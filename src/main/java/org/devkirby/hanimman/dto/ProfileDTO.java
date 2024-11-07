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
public class ProfileDTO {
    private Integer id;
    private String realName;
    private String serverName;
    private String mineType;
    private Integer fileSize;
    private Instant createdAt;
    private Integer parentId;
}
