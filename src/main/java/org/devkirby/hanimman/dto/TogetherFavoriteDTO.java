package org.devkirby.hanimman.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherFavoriteDTO {
    private Integer id;
    private Integer userId;  // User의 ID
    private Integer togetherId;  // Together의 ID
    private Instant createdAt;
}
