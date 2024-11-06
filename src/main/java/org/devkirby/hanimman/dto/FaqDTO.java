package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Faq;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {
    private Integer id;
    private String title;
    private String content;
    private int views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private Integer userId;  // User 엔티티의 ID만 포함

    public static FaqDTO fromEntity(Faq entity) {
        return FaqDTO.builder()
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
