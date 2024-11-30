package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.entity.UserAddress;
import org.devkirby.hanimman.repository.AddressRepository;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserAddressService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;

    public void firstSaveUserAddressRepository(UserAddressDTO userAddressDTO){
        String currentUserAddress = userAddressDTO.getPrimaryAddressId();
        Integer userId = userAddressDTO.getUserId();
        Optional<User> opt = userRepository.findById(userId);
        User user = opt.orElseThrow();

        Optional<Address> addressOpt = addressRepository.findById(currentUserAddress);
        Address address =addressOpt.orElseThrow();
        UserAddress userAddress = UserAddress.builder()
                .primaryAddress(address)
                .secondlyAddress(null)
                .user(user)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .validatedAt(null)
                .build();

        userAddressRepository.save(userAddress);

    }

}
