package org.devkirby.hanimman.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeFileDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer fileSize;
    private Instant createdAt;
    private Instant deletedAt;
    private Integer userId;  // 사용자 ID (User 엔티티의 ID)
    private Integer noticeId;  // Notice 엔티티의 ID
}
