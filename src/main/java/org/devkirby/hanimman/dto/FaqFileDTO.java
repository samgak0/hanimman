package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqFileDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer userId;  // User ID만 전달
    private Integer fileSize;
    private Integer parentId; // Faq ID만 전달
    private Instant createdAt;
}
