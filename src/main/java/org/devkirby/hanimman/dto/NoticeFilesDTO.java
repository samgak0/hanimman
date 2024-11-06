package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.NoticeFiles;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeFilesDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer userId;  // User 엔티티의 ID만 포함
    private Integer fileSize;
    private Integer parentId;  // Notices 엔티티의 ID만 포함
    private Instant createdAt;
    private Instant deletedAt;

    public static NoticeFilesDTO fromEntity(NoticeFiles entity) {
        return NoticeFilesDTO.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .serverName(entity.getServerName())
                .mineType(entity.getMineType())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .fileSize(entity.getFileSize())
                .parentId(entity.getParentId().getId())  // Notices 엔티티의 ID를 추출
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}

