package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareFavoriteDTO {
    private Integer id;
    private Integer userId; // User의 ID만 전달
    private Integer shareId; // Share의 ID만 전달
    private Instant createdAt;
}
