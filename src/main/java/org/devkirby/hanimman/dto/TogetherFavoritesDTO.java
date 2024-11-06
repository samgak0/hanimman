package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.TogetherFavorites;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherFavoritesDTO {
    private Integer id;
    private Integer userId;  // User 엔티티의 ID만 포함
    private Integer parentId;  // Together 엔티티의 ID만 포함
    private Instant createdAt;

    public static TogetherFavoritesDTO fromEntity(TogetherFavorites entity) {
        return TogetherFavoritesDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .parentId(entity.getParentId().getId())  // Together 엔티티의 ID를 추출
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
