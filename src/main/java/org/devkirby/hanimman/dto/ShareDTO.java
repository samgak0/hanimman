package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Share;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareDTO {

    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private String location;
    private Instant locationDate;
    private String item;
    private Integer quantity;
    private boolean isEnd;
    private Integer userId;  // User ID만 포함


    public static ShareDTO fromEntity(Share share) {
        return ShareDTO.builder()
                .id(share.getId())
                .title(share.getTitle())
                .content(share.getContent())
                .views(share.getViews())
                .createdAt(share.getCreatedAt())
                .location(share.getLocation())
                .locationDate(share.getLocationDate())
                .item(share.getItem())
                .quantity(share.getQuantity())
                .isEnd(share.isEnd())
                .userId(share.getUserId().getId())  // User 객체의 ID만 가져옴
                .build();
    }
}