package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.Gender;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.entity.UserAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class UserAddressRepositoryTests {
     @Autowired
     private AddressRepository addressRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Test
    void init(){

    }
    @Test
    @Transactional
    void insertAddressTest(){
        User user = User.builder()
                .id(2)
                .name("이용준")
                .birth(LocalDate.parse("1994-03-09"))
                .gender(Gender.M)
                .phonenum("010-3682-2901")
                .nickname("Dragon01")
                .codenum("dieRlq")
                .build();

        Optional<Address> opt = addressRepository.findById("2635010400");
        Address address = opt.orElseThrow();

        UserAddress userAddress = UserAddress.builder()
                .user(user)
                .primaryAddress(address)
                .secondlyAddress(null)
                .validatedAt(null)
                .modifiedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        userAddressRepository.save(userAddress);
    }
}
