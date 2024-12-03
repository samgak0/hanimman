package org.devkirby.hanimman.util;

import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserAddressUtil {

    @Autowired
    private UserAddressService service;

    public UserAddressDTO getAddress(Integer id){
        Optional<UserAddressDTO> opt = service.getUserAddress(id);
        UserAddressDTO userAddressDTO = opt.orElseThrow();
        return  userAddressDTO;
    }
}
