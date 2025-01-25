package org.devkirby.hanimman.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주소 정보를 관리하는 엔티티
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "addresses")
public class Address {

    /**
     * 주소의 고유 식별자
     */
    @Id
    @Column(nullable = false, length = 10)
    private String id;

    /**
     * 도시 이름
     */
    @Column(length = 50, nullable = false)
    private String cityName;

    /**
     * 구/군 이름
     */
    @Column(length = 50)
    private String districtName;

    /**
     * 동네 이름
     */
    @Column(length = 50)
    private String neighborhoodName;

    /**
     * 마을 이름
     */
    @Column(length = 50)
    private String villageName;

    /**
     * 도시 코드
     */
    @Column(length = 50, nullable = false)
    private String cityCode;

    /**
     * 구/군 코드
     */
    @Column(length = 50)
    private String districtCode;

    /**
     * 동네 코드
     */
    @Column(length = 50)
    private String neighborhoodCode;

    /**
     * 마을 코드
     */
    @Column(length = 50)
    private String villageCode;
}