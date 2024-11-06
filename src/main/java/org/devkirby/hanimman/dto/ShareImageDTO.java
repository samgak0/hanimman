package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareImageDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer fileSize;
    private Integer userId;  // User의 ID만 전달
    private Integer shareId; // Share의 ID만 전달
    private Instant deletedAt;
    private Instant createdAt;
}
