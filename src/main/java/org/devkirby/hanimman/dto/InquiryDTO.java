package org.devkirby.hanimman.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private Integer userId;  // 사용자 ID (User 엔티티의 ID)
}
