package org.devkirby.hanimman.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherParticipantDTO {
    private Integer id;
    private Instant date;
    private Integer quantity;
    private Instant rejected;
    private Instant accepted;
    private Instant complete;
    private Integer userId;  // User의 ID
    private Integer parentId;  // Together의 ID
    private Instant deletedAt;
}
