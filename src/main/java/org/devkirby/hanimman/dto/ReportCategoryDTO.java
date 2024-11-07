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
public class ReportCategoryDTO {
    private Integer id;
    private String name;
    private Integer managerId;
    private Instant createDate;
    private Instant deletedAt;
}
