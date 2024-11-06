package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryFileDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer fileSize;
    private Integer userId;  // 사용자 ID (User 엔티티의 ID)
    private Integer inquiryId;  // 문의 ID (Inquiry 엔티티의 ID)
    private Instant createdAt;
    private Instant deletedAt;
}
