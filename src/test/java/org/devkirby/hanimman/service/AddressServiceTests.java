package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.AddressDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.repository.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AddressServiceTests {

    @Mock
    private AddressRepository addressRepository; // AddressRepository를 Mocking

    @InjectMocks
    private AddressService addressService; // AddressService에 Mock을 주입

//    @Test
//    @DisplayName("주소 정보 조회 테스트")
//    public void testGetAddress() {
//        // Given: Address 객체 생성
//        Address address = Address.builder()
//                .id("2635010500")
//                .cityName("부산광역시")
//                .districtName("해운대구")
//                .neighborhoodName("우동")
//                .villageName("")
//                .cityCode("26")
//                .districtCode("350")
//                .neighborhoodCode("10500")
//                .villageCode(null)
//                .build();
//
//        // When: Mocking repository의 동작 설정
//        when(addressRepository.findById("2635010500")).thenReturn(Optional.of(address));
//
//        // Then: 서비스 메서드 호출 및 결과 검증
//        AddressDTO addressDTO = addressService.getAdministrativeArea(35.172918, 129.130723); // 위도와 경도는 예시
//        assertNotNull(addressDTO);
//        assertEquals("부산광역시", addressDTO.getCityName());
//        assertEquals("해운대구", addressDTO.getDistrictName());
//        assertEquals("우동", addressDTO.getNeighborhoodName());
//        assertEquals("", addressDTO.getVillageName());
//    }
}
