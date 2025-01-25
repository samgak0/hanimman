package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주소 정보를 담는 DTO 클래스입니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    /** 법정동 코드 (예: 1111010100) */
    private String id; // 법정동 코드 (1111010100)
    /** 시 이름 (예: 서울특별시) */
    private String cityName; // 시 이름(서울특별시)
    /** 구 이름 (예: 종로구) */
    private String districtName; // 구 이름 (종로구)
    /** 동 이름 (예: 청운동) */
    private String neighborhoodName; // 동 이름 (청운동)
    /** 리 이름 (예: 청운효자동 또는 null) */
    private String villageName; // 리 이름 (청운효자동 or null)
    /** 시 코드 (예: 11) */
    private String cityCode; // 시 코드 (11)
    /** 구 코드 (예: 110) */
    private String districtCode; // 구 코드 (110)
    /** 동 코드 (예: 10100) */
    private String neighborhoodCode; // 동 코드 (10100)
    /** 리 코드 (예: null) */
    private String villageCode; // 리 코드 (null)
}
