package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Together;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private String meetingLocation;
    private Instant meetingAt;
    private String item;
    private Integer quantity;
    private boolean isEnd;
    private Integer userId;  // User 엔티티의 ID만 포함

    public static TogetherDTO fromEntity(Together entity) {
        return TogetherDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .views(entity.getViews())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .deletedAt(entity.getDeletedAt())
                .meetingLocation(entity.getMeetingLocation())
                .meetingAt(entity.getMeetingAt())
                .item(entity.getItem())
                .quantity(entity.getQuantity())
                .isEnd(entity.isEnd())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .build();
    }
}
