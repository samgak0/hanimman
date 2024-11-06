package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.ShareReports;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareReportsDTO {
    private Integer id;
    private Integer reporterId;  // User 엔티티의 ID만 포함
    private Integer reportedId;  // User 엔티티의 ID만 포함
    private Integer categoryId;  // ReportCategory 엔티티의 ID만 포함
    private Integer parentId;    // Share 엔티티의 ID만 포함

    public static ShareReportsDTO fromEntity(ShareReports entity) {
        return ShareReportsDTO.builder()
                .id(entity.getId())
                .reporterId(entity.getReporterId().getId())  // User 엔티티의 ID를 추출
                .reportedId(entity.getReportedId().getId())  // User 엔티티의 ID를 추출
                .categoryId(entity.getCategory().getId())    // ReportCategory 엔티티의 ID를 추출
                .parentId(entity.getParentId().getId())      // Share 엔티티의 ID를 추출
                .build();
    }
}
