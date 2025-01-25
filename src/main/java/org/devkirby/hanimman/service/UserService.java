package org.devkirby.hanimman.service;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.UserDTO;

public interface UserService{
    UserDTO createUser(UserDTO userDTO);
    UserDTO selectUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(UserDTO userDTO);
    boolean isExistCodeNum(String codenum);
    CustomUserDetails loadUserByCodeNum(String codenum);
    UserDTO getCurrentUserDetails(CustomUserDetails customUserDetails);
}
