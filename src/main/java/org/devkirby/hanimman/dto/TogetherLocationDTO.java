package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherLocationDTO {
    private Integer id;
    private String latitude;
    private String longitude;
    private String address;
    private String detail;
    private Integer togetherId;
    private String createdAt;
    private String modifiedAt;
}
