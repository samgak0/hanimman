package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareParticipantDTO {
    private Integer id;
    private Instant date;
    private Integer quantity;
    private Instant rejected;
    private Instant accepted;
    private Instant complete;
    private Integer userId;   // User의 ID만 전달
    private Integer parentId;  // Share의 ID만 전달
    private Instant deletedAt;
}
