package org.devkirby.hanimman.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer views;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant deletedAt;
    private Integer userId;  // 사용자 ID (User 엔티티의 ID)

    // 파일 리스트 추가
    private List<String> fileUrls;
}
