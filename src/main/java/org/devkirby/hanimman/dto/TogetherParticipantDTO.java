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
public class TogetherParticipantDTO {
    private Integer id;
    private Instant date;
    private Integer quantity;
    private Boolean rejected;
    private Integer userId;  // User의 ID
    private Integer togetherId;  // Together의 ID
    private Instant deletedAt;
}
