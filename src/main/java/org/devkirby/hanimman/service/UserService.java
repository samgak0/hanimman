package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    //회원 생성
    public UserDTO createUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    //회원조회
    public UserDTO selectUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        Optional<User> opt = userRepository.findById(user.getId());
        User savedUser = new User();
        if(opt.isPresent()) {
            savedUser = opt.get();
        }
        return modelMapper.map(savedUser, UserDTO.class);
    }

    //회원수정
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        Optional<User> opt = userRepository.findById(user.getId());
        if (opt.isPresent()) {
            User existingUser = opt.get();

            // 기존 사용자 정보 업데이트
            existingUser.setName(user.getName());
            existingUser.setNickname(user.getNickname());
            existingUser.setPrimaryRegion(user.getPrimaryRegion());
            existingUser.setSecondlyRegion(user.getSecondlyRegion());
            existingUser.setDeviceUniqueNum(user.getDeviceUniqueNum());

            // 업데이트된 사용자 저장
            User updatedUser = userRepository.save(existingUser);
            return modelMapper.map(updatedUser, UserDTO.class);
        } else {
            System.out.println("회원를 찾을수 없습니다" + userDTO.getId());
            return null;
//          throw new UserNotFoundException("회원을 찾을 수 없습니다: " + userDTO.getId());
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getId() == null) {
            throw new IllegalArgumentException("userDTO 또는 사용자 ID는 null일 수 없습니다.");
        }

        Optional<User> opt = userRepository.findById(userDTO.getId());

        if (opt.isPresent()) {
            userRepository.delete(opt.get());
            System.out.println("회원을 성공적으로 삭제되었습니다: " + userDTO.getId());
        } else {
            System.out.println("회원을 찾을 수 없습니다: " + userDTO.getId());
//            throw new UserNotFoundException("회원을 찾을 수 없습니다: " + userDTO.getId());
        }
    }
}
