package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.InquireFiles;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquireFilesDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer userId;  // User 엔티티의 ID만 포함
    private Integer fileSize;
    private Integer parentId;  // Inquiries 엔티티의 ID만 포함
    private Instant createdAt;
    private Instant deletedAt;

    public static InquireFilesDTO fromEntity(InquireFiles entity) {
        return InquireFilesDTO.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .serverName(entity.getServerName())
                .mineType(entity.getMineType())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .fileSize(entity.getFileSize())
                .parentId(entity.getParentId().getId())  // Inquiries 엔티티의 ID만 추출
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
