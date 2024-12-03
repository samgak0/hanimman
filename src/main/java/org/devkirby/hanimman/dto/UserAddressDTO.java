package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDTO {
    Integer id;
    Integer userId;
    String primaryAddressId;
    String secondlyAddressId;
    LocalDateTime validatedAt;
    LocalDateTime modifiedAt;
    LocalDateTime createdAt;
}
