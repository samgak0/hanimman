package org.devkirby.hanimman.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.TogetherParticipants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherParticipantsDTO {
    private Integer id;
    private Instant date;
    private Integer quantity;
    private boolean rejected;
    private Integer userId;  // User 엔티티의 ID만 포함
    private Integer parentId;  // Together 엔티티의 ID만 포함

    public static TogetherParticipantsDTO fromEntity(TogetherParticipants entity) {
        return TogetherParticipantsDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .quantity(entity.getQuantity())
                .rejected(entity.isRejected())
                .userId(entity.getUserId().getId())  // User 엔티티의 ID를 추출
                .parentId(entity.getParent().getId())  // Together 엔티티의 ID를 추출
                .build();
    }
}

