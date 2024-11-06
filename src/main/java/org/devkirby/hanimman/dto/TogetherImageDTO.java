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
public class TogetherImageDTO {
    private Integer id;
    private String originalName;
    private String serverName;
    private String mineType;
    private Integer userId;  // User의 ID
    private Integer fileSize;
    private Integer togetherId;  // Together의 ID
    private Instant createdAt;
    private Instant deletedAt;
}
