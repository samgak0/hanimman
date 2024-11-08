package org.devkirby.hanimman.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "addresses")
public class Address {

    @Id
    @Column(nullable = false, length = 10)
    private String id;

    @Column(length = 50, nullable = false)
    private String cityName;

    @Column(length = 50)
    private String districtName;

    @Column(length = 50)
    private String neighborhoodName;

    @Column(length = 50)
    private String villageName;

    @Column(length = 50, nullable = false)
    private String cityCode;

    @Column(length = 50)
    private String districtCode;

    @Column(length = 50)
    private String neighborhoodCode;

    @Column(length = 50)
    private String villageCode;
}