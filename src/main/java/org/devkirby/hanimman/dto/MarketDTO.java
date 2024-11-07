package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketDTO {
    private Integer id;
    private Integer category;
    private String name;
    private String latitude;
    private String longitude;
    private Integer cityId;
    private Integer CountryId;
    private Integer regionId;
    private String address;
}
