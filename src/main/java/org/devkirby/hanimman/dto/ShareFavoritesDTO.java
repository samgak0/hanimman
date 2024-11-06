package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.ShareFavorites;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareFavoritesDTO {
    private Integer id;
    private Integer userId;      // User 엔티티의 ID만 사용
    private Integer parentId;    // Share 엔티티의 ID만 사용
    private Instant createdAt;

    public static ShareFavoritesDTO fromEntity(ShareFavorites entity) {
        return ShareFavoritesDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID 추출
                .parentId(entity.getParentId().getId()) // Share 엔티티의 ID 추출
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

