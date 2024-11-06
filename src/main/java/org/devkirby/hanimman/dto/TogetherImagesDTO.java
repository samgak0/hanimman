package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.TogetherImages;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherImagesDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private Integer fileSize;
    private Integer userId;  // User 엔티티의 ID만 포함
    private Integer parentId;  // Together 엔티티의 ID만 포함
    private Instant createdAt;
    private Instant deletedAt;

    public static TogetherImagesDTO fromEntity(TogetherImages entity) {
        return TogetherImagesDTO.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .serverName(entity.getServerName())
                .fileSize(entity.getFileSize())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .parentId(entity.getParent().getId())  // Together 엔티티의 ID를 추출
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
