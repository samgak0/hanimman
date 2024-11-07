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
public class ChatParticipantDTO {

    private Integer id;
    private Integer roomId;
    private Integer userId;
    private Instant joinedAt;
}
