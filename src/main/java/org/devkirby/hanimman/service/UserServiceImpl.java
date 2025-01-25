package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 회원 생성
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = modelMapper.map(userDTO, User.class);
            User savedUser = userRepository.save(user);
            log.info("User saved: " + savedUser);
            return modelMapper.map(savedUser, UserDTO.class); // 저장된 사용자 정보를 DTO로 변환하여 반환
        } catch (Exception e) {
            log.error("Error during user creation", e);
            throw new RuntimeException("회원가입 중 오류가 발생했습니다.", e);
        }
    }

    // 회원조회
    public UserDTO selectUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Optional<User> opt = userRepository.findByNameAndPhonenumAndGenderAndBirth(userDTO.getName(),
                userDTO.getPhonenum(), userDTO.getGender(), userDTO.getBirth());
        User savedUser = new User();
        if (opt.isPresent()) {
            savedUser = opt.get();
        }
        return modelMapper.map(savedUser, UserDTO.class);
    }

    // 회원수정
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        Optional<User> opt = userRepository.findById(user.getId());
        if (opt.isPresent()) {
            User existingUser = opt.get();

            // 기존 사용자 정보 업데이트
            existingUser.setName(user.getName());
            existingUser.setNickname(user.getNickname());


            // 업데이트된 사용자 저장
            User updatedUser = userRepository.save(existingUser);
            return modelMapper.map(updatedUser, UserDTO.class);
        } else {
            System.out.println("회원를 찾을수 없습니다" + userDTO.getId());
            return null;
            // throw new UserNotFoundException("회원을 찾을 수 없습니다: " + userDTO.getId());
        }
    }

    // 회원 blocked
    @Transactional
    public void blockedUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getId() == null) {
            throw new IllegalArgumentException("회원 데이터가 존재하지 않습니다.");
        }
        // 사용자 ID로 조회
        Optional<User> opt = userRepository.findById(userDTO.getId());

        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getBlockedAt() != null) {
            } else {
                user.setBlockedAt(Instant.now());
                userRepository.save(user);
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getId() == null) {
            throw new IllegalArgumentException("userDTO가 없거나 ID가 없을 경우");
        }

        // 사용자 ID로 조회
        Optional<User> opt = userRepository.findById(userDTO.getId());

        if (opt.isPresent()) {
            User user = opt.get();
            // 탈퇴일 확인
            if (user.getDeletedAt() != null) {
                // 이미 탈퇴한 사용자 처리
            } else {
                user.setDeletedAt(Instant.now());
                userRepository.save(user); // 변경된 사용자 정보를 저장
            }
        } else {
            // 사용자를 찾지 못한 경우
            System.out.println("회원을 찾을 수 없습니다: " + userDTO.getId());
        }
    }

    @Override
    public boolean isExistCodeNum(String codenum) {
        return userRepository.existsByCodenum(codenum);
    }

    @Override
    public CustomUserDetails loadUserByCodeNum(String codenum) {
        Optional<User> opt = userRepository.findByCodenum(codenum);
        if (opt.isPresent()) {
            User user = opt.get();
            // 사용자 권한 설정
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            // CustomUserDetails 객체 생성 후 반환
            return new CustomUserDetails(user.getId(), user.getNickname(), user.getCodenum(), authorities);
        } else {
            throw new RuntimeException("User not found with codenum: " + codenum);
        }
    }


    @Override
    public UserDTO getCurrentUserDetails(CustomUserDetails customUserDetails) {
        Integer id = customUserDetails.getId();
        Optional<User> opt = userRepository.findById(id);
        User user = opt.orElseThrow();
        return modelMapper.map(user, UserDTO.class);
    }

}
