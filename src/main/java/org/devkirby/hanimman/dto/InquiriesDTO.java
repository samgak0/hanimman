package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Inquiries;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiriesDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private Integer userId;  // User 엔티티의 ID만 포함

    public static InquiriesDTO fromEntity(Inquiries entity) {
        return InquiriesDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .views(entity.getViews())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .deletedAt(entity.getDeletedAt())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .build();
    }
}

