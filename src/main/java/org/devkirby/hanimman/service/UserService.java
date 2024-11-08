package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    // 회원 생성
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    // 회원조회
    public UserDTO selectUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Optional<User> opt = userRepository.findById(user.getId());
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
            existingUser.setPrimaryAddress(user.getPrimaryAddress());
            existingUser.setSecondlyAddress(user.getSecondlyAddress());
            existingUser.setDeviceUnique(user.getDeviceUnique());

            // 업데이트된 사용자 저장
            User updatedUser = userRepository.save(existingUser);
            return modelMapper.map(updatedUser, UserDTO.class);
        } else {
            System.out.println("회원를 찾을수 없습니다" + userDTO.getId());
            return null;
            // throw new UserNotFoundException("회원을 찾을 수 없습니다: " + userDTO.getId());
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
                System.out.println("이 사용자는 이미 탈퇴한 상태입니다: " + userDTO.getId());
            } else {

                user.setDeletedAt(Instant.now());
                userRepository.save(user); // 변경된 사용자 정보를 저장
                System.out.println("회원을 성공적으로 탈퇴하였습니다: " + userDTO.getId());
            }
        } else {
            // 사용자를 찾지 못한 경우
            System.out.println("회원을 찾을 수 없습니다: " + userDTO.getId());
        }
    }
}
