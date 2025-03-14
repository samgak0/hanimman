package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.dto.AddressDTO;
import org.devkirby.hanimman.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 위도경도를 통해 지역정보를 반환(법정코드포함)
     * 
     * @param latitude  위도
     * @param longitude 경도
     * @return 지역정보
     */
    @GetMapping("/administrative")
    public ResponseEntity<AddressDTO> getAdministrativeArea(@RequestParam double latitude,
            @RequestParam double longitude) {
        System.out.println("Received request with latitude: " + latitude + " and longitude: " + longitude);
        AddressDTO addressDTO = addressService.getAdministrative(latitude, longitude);
        return ResponseEntity.ok(addressDTO);
    }

    /**
     * 주소 검색
     * 
     * @param query 검색어
     * @return 주소 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<AddressDTO>> searchAddresses(@RequestParam String query) {
        List<AddressDTO> addresses = addressService.searchByNeighborhood(query);
        System.out.println(addresses);
        return ResponseEntity.ok(addresses);
    }
}
