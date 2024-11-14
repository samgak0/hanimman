package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.dto.AddressDTO;
import org.devkirby.hanimman.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/administrative") //위도경도를 통해 지역정보를 반환(법정코드포함)
    public ResponseEntity<AddressDTO> getAdministrativeArea(@RequestParam double latitude, @RequestParam double longitude) {
        AddressDTO addressDTO = addressService.getAdministrativeArea(latitude, longitude);
        return ResponseEntity.ok(addressDTO);
    }

}
