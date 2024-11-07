package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherReviewDTO {
    private Integer id;
    private String content;
    private Instant createdAt;
    private Instant deletedAt;
    private Integer userId;
    private Integer parentId;

}
