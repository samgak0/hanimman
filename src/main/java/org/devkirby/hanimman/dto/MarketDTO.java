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
    private String cityCode;
    private String districtCode;
    private String neighborhoodCode;
    private String addressDetail;
    private String addressId;
}
