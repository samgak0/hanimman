package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    // 주소 생성
    @PostMapping
    public ResponseEntity<UserAddressDTO> createUserAddress(@RequestBody UserAddressDTO userAddressDTO, @AuthenticationPrincipal CustomUserDetails loginUser) {
        userAddressDTO.setUserId(loginUser.getId());
        UserAddressDTO savedAddress = userAddressService.saveUserAddress(userAddressDTO);
        System.out.println(savedAddress + "뭐가찍힘?");
        return ResponseEntity.ok(savedAddress);
    }

    // 주소 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserAddressDTO> getUserAddress(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails loginUser) {
        return userAddressService.getUserAddress(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 모든 주소 조회
    @GetMapping
    public ResponseEntity<List<UserAddressDTO>> getAllUserAddresses(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<UserAddressDTO> addresses = userAddressService.getAllUserAddresses(loginUser.getId());
        return ResponseEntity.ok(addresses);
    }

    // 주소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails loginUser) {
        userAddressService.deleteUserAddress(id);
        return ResponseEntity.noContent().build();
    }
}
