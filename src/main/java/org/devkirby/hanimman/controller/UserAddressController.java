package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ResponseUserAddressDTO;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.UserAddress;
import org.devkirby.hanimman.repository.AddressRepository;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.devkirby.hanimman.service.AddressService;
import org.devkirby.hanimman.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {
    private final Logger log = LoggerFactory.getLogger(TogetherController.class);

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAddressService userAddressService;
    private CustomUserDetails customUserDetails;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;

    // 첫번째 주소 생성
    @PostMapping("/save")
    public ResponseEntity<UserAddressDTO> saveUserAddress(@RequestBody UserAddressDTO userAddressDTO, @AuthenticationPrincipal CustomUserDetails loginUser) {
        System.out.println(loginUser);
        userAddressDTO.setUserId(loginUser.getId());
        UserAddressDTO savedAddress = userAddressService.saveUserAddress(userAddressDTO);

//        System.out.println(savedAddress + "무슨값이 있나요??");
        return ResponseEntity.ok(savedAddress);
    }

    // 두 번째 주소 저장
    @PostMapping("/save/secondary")
    public ResponseEntity<UserAddressDTO> saveSecondaryUserAddress(@RequestBody UserAddressDTO userAddressDTO, @AuthenticationPrincipal CustomUserDetails loginUser) {
        System.out.println(loginUser + "???????????????");
        userAddressDTO.setUserId(loginUser.getId());
        UserAddressDTO savedAddress = userAddressService.saveSecondaryUserAddress(userAddressDTO);
        System.out.println(savedAddress + "무슨값이 있음?");
        return ResponseEntity.ok(savedAddress);
    }

    //주소 조회
    @GetMapping("/select")
    public ResponseEntity<ResponseUserAddressDTO> getUserAddress(@AuthenticationPrincipal CustomUserDetails loginUser) {
        String secondAddressName = null;
        String secondlyAddressId = null;

        Optional<UserAddressDTO> addresses = userAddressService.getUserAddress(loginUser.getId());
        UserAddressDTO userAddressDTO = addresses.orElseThrow();
        System.out.println(addresses + "주소가 뭐가 있나요?");

        String primaryAddressName = userAddressService.selectUserAddressName(userAddressDTO.getPrimaryAddressId());
        if(userAddressDTO.getSecondlyAddressId() != null){
            secondlyAddressId = userAddressDTO.getSecondlyAddressId();
            secondAddressName = userAddressService.selectUserAddressName(secondlyAddressId);
        }
        ResponseUserAddressDTO.ResponseUserAddressDTOBuilder builder = ResponseUserAddressDTO.builder()
                .primaryAddressId(userAddressDTO.getPrimaryAddressId())
                .primaryAddressName(primaryAddressName);

        // secondAddressId가 null이 아닐 경우에만 추가
        if (secondlyAddressId != null) {
            builder.secondlyAddressId(secondlyAddressId);
            builder.secondAddressName(secondAddressName);
        }

        Address address1 = addressRepository.findById(userAddressDTO.getPrimaryAddressId()).orElseThrow();
        if(userAddressDTO.getSecondlyAddressId() != null){
            Address address2 = addressRepository.findById(userAddressDTO.getSecondlyAddressId()).orElseThrow();
            builder.secondNeighborhoodName(address2.getNeighborhoodName());
        }

        // 빌더로 DTO 생성
        ResponseUserAddressDTO responseUserAddressDTO = builder.build();
        responseUserAddressDTO.setPrimaryNeighborhoodName(address1.getNeighborhoodName());


        return ResponseEntity.ok(responseUserAddressDTO);
    }

    // 주소 수정
    @PutMapping("/update")
    public ResponseEntity<UserAddressDTO> updateUserAddress(
            @RequestBody UserAddressDTO userAddressDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {

        log.info("주소 업데이트 요청: {}", userAddressDTO);
        userAddressDTO.setId(userAddressRepository.findByUserId(loginUser.getId()).getId());
        userAddressDTO.setUserId(loginUser.getId());

        log.info("주소 업데이트 요청2: {}", userAddressDTO);

        // userAddressService를 사용하여 주소를 가져옴
        UserAddressDTO existingAddress = userAddressService.getUserAddress(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("주소를 찾을 수 없습니다."));

        // 주소 업데이트 서비스 호출
        UserAddressDTO updatedAddress = userAddressService.updateUserAddress(userAddressDTO);

        // 업데이트된 주소 반환
        return ResponseEntity.ok(updatedAddress);
    }
}
