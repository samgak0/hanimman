package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.TogetherReports;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherReportsDTO {
    private Integer id;
    private Integer reporterId;  // User 엔티티의 ID만 포함
    private Integer reportedId;  // User 엔티티의 ID만 포함
    private Integer categoryId;  // ReportCategory 엔티티의 ID만 포함
    private Integer parentId;    // Share 엔티티의 ID만 포함

    public static TogetherReportsDTO fromEntity(TogetherReports entity) {
        return TogetherReportsDTO.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId().getId())  // User 엔티티의 ID를 추출
                .reportedId(entity.getReportedId().getId())  // User 엔티티의 ID를 추출
                .categoryId(entity.getCategory().getId())    // ReportCategory 엔티티의 ID를 추출
                .parentId(entity.getParentId().getId())      // Share 엔티티의 ID를 추출
                .build();
    }
}

