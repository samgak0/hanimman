package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;


public interface UserService{
    void createUser(UserDTO userDTO);
    UserDTO selectUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(UserDTO userDTO);
    boolean isExistCodeNum(String codenum);
}
