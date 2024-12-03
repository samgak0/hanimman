package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDTO {
   private Integer id;
   private Integer userId;
   private String primaryAddressId;
   private String secondlyAddressId;
   private Instant validatedAt;
   private Instant modifiedAt;
   private Instant createdAt;
}
