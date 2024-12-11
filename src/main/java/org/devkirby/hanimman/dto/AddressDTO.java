package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private String id; // 법정동 코드 (1111010100)
    private String cityName; // 시 이름(서울특별시)
    private String districtName; // 구 이름 (종로구)
    private String neighborhoodName; // 동 이름 (청운동)
    private String villageName; // 리 이름 (청운효자동 or null)
    private String cityCode; // 시 코드 (11)
    private String districtCode; // 구 코드 (110)
    private String neighborhoodCode; // 동 코드 (10100)
    private String villageCode; // 리 코드 (null)


}
