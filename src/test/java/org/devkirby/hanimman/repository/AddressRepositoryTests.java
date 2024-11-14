package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(AddressRepositoryTests.class);

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("주소 정보 입력 테스트")
    public void testSaveAddress() {
        // Given: Address 객체 생성
        Address address = Address.builder()
                .id("2635010500") // 법정동 코드
                .cityName("부산광역시") // 시 이름
                .districtName("해운대구") // 구 이름
                .neighborhoodName("우동") // 동 이름
                .villageName("") // 리 이름 (optional)
                .cityCode("26") // 시 코드
                .districtCode("350") // 구 코드
                .neighborhoodCode("10500") // 동 코드
                .villageCode(null) // 리 코드 (optional)
                .build();

        // When: 주소 정보 저장
        addressRepository.save(address);
        // Then: DB에서 해당 값이 잘 저장되었는지 확인
        Optional<Address> foundAddress = addressRepository.findById("2635010500");
        assertTrue(foundAddress.isPresent(), "주소 정보가 저장되지 않았습니다.");

        Address retrievedAddress = foundAddress.get();
        assertEquals("부산광역시", retrievedAddress.getCityName());
        assertEquals("해운대구", retrievedAddress.getDistrictName());
        assertEquals("우동", retrievedAddress.getNeighborhoodName());
        assertEquals("", retrievedAddress.getVillageName());
        assertEquals("26", retrievedAddress.getCityCode());
        assertEquals("350", retrievedAddress.getDistrictCode());
    }

    @Test
    void findAddress(){
        Optional<Address> address = addressRepository.findById("2635010500");
        Address SelectAddress = address.orElseThrow();
        System.out.println(SelectAddress);
    }


}
