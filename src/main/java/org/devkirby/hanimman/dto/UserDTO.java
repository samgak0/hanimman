package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Gender;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private LocalDate birth;
    private Gender gender;
    private String phonenum;
    private String nickname;
    private String codenum;
    private Boolean privilege;
    private Instant blockedAt;
    private Integer primaryRegionId;
    private Integer secondlyRegionId;
    private String deviceUniqueNum;
    private Instant modifiedAt;
    private Instant createdAt;
    private Instant deletedAt;
}
