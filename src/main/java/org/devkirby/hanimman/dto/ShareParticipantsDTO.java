package org.devkirby.hanimman.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.ShareParticipants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareParticipantsDTO {
    private Integer id;
    private LocalDateTime date;
    private Integer quantity;
    private boolean rejected;
    private Integer userId;
    private Integer parentId;

    public static ShareParticipantsDTO fromEntity(ShareParticipants entity) {
        return ShareParticipantsDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .quantity(entity.getQuantity())
                .rejected(entity.isRejected())
                .userId(entity.getUserId().getId())
                .parentId(entity.getParentId().getId())
                .build();
    }
}