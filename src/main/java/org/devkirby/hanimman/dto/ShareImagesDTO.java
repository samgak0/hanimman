package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.ShareImages;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareImagesDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private Integer fileSize;
    private Integer userId;
    private Integer parentId;
    private Instant createdAt;
    private Instant deletedAt;

    public static ShareImagesDTO fromEntity(ShareImages entity) {
        return ShareImagesDTO.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .serverName(entity.getServerName())
                .fileSize(entity.getFileSize())
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
