package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserAddressDTO {
    private Integer id;
    private Integer userId;
    private String primaryAddressId;
    private String secondlyAddressId;
    private String primaryAddressName;
    private String secondAddressName;
}
