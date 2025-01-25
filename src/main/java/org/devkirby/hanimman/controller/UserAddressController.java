package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ResponseUserAddressDTO;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.repository.AddressRepository;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.service.AddressService;
import org.devkirby.hanimman.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {
    private final Logger log = LoggerFactory.getLogger(TogetherController.class);

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private AddressRepository addressRepository;

    // 첫번째 주소 생성
    @PostMapping("/save")
    public ResponseEntity<UserAddressDTO> saveUserAddress(@RequestBody UserAddressDTO userAddressDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        System.out.println(loginUser);
        userAddressDTO.setUserId(loginUser.getId());
        UserAddressDTO savedAddress = userAddressService.saveUserAddress(userAddressDTO);

        // System.out.println(savedAddress + "무슨값이 있나요??");
        return ResponseEntity.ok(savedAddress);
    }

    // 두 번째 주소 저장
    @PostMapping("/save/secondary")
    public ResponseEntity<UserAddressDTO> saveSecondaryUserAddress(@RequestBody UserAddressDTO userAddressDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        // System.out.println(loginUser + "???????????????");
        userAddressDTO.setUserId(loginUser.getId());
        UserAddressDTO savedAddress = userAddressService.saveSecondaryUserAddress(userAddressDTO);
        // System.out.println(savedAddress + "무슨값이 있음?");
        return ResponseEntity.ok(savedAddress);
    }

    // 주소 조회
    @GetMapping("/select")
    public ResponseEntity<ResponseUserAddressDTO> getUserAddress(@AuthenticationPrincipal CustomUserDetails loginUser) {
        String secondAddressName = null;
        String secondlyAddressId = null;

        Optional<UserAddressDTO> addresses = userAddressService.getUserAddress(loginUser.getId());
        UserAddressDTO userAddressDTO = addresses.orElseThrow();
        // System.out.println(addresses + "주소가 뭐가 있나요?");

        String primaryAddressName = userAddressService.selectUserAddressName(userAddressDTO.getPrimaryAddressId());
        if (userAddressDTO.getSecondlyAddressId() != null) {
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
        if (userAddressDTO.getSecondlyAddressId() != null) {
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
        userAddressDTO.setUserId(loginUser.getId());

        // 기존 주소 조회
        Optional<UserAddressDTO> existingAddressOpt = userAddressService.getUserAddress(loginUser.getId());

        if (existingAddressOpt.isPresent()) {
            // 기존 주소가 있을 경우 업데이트
            userAddressDTO.setId(existingAddressOpt.get().getId());
            log.info("주소 업데이트 요청2: {}", userAddressDTO);

            // 주소 업데이트 서비스 호출
            UserAddressDTO updatedAddress = userAddressService.updateUserAddress(userAddressDTO);
            return ResponseEntity.ok(updatedAddress);
        } else {
            // 기존 주소가 없으면 신규 주소 저장 요청
            UserAddressDTO savedAddress = userAddressService.saveUserAddress(userAddressDTO);
            log.info("신규 주소 저장: {}", savedAddress);
            return ResponseEntity.ok(savedAddress);
        }
    }

}
