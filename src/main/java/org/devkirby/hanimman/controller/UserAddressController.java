package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;
    private CustomUserDetails customUserDetails;

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
    public ResponseEntity<List<UserAddressDTO>> getUserAddress(@AuthenticationPrincipal CustomUserDetails loginUser) {
        System.out.println("-----------------------유저 address select" + loginUser);
        Optional<UserAddressDTO> addresses = userAddressService.getUserAddress(loginUser.getId());
        System.out.println("--------------------주소" + addresses);
        return ResponseEntity.noContent().build();
    }

    // 주소 수정
    @PutMapping("/update")
    public ResponseEntity<UserAddressDTO> updateUserAddress(@RequestBody UserAddressDTO userAddressDTO) {
        try {
            UserAddressDTO updatedAddress = userAddressService.updateUserAddress(userAddressDTO);
            return ResponseEntity.ok(updatedAddress); // 성공적으로 업데이트된 주소 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 요청 처리
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 주소 정보를 찾을 수 없는 경우
        }
    }
    
    
    // 주소 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUserAddress(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails loginUser) {
//        userAddressService.deleteUserAddress(id);
//        return ResponseEntity.noContent().build();
//    }
}
