package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDTO {
    Integer id;
    Integer userId;
    Integer primaryAddressId;
    Integer secondlyAddressId;
    LocalDateTime validatedAt;
    LocalDateTime modifiedAt;
    LocalDateTime createdAt;
}
