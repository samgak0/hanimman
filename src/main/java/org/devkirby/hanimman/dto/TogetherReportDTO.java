package org.devkirby.hanimman.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TogetherReportDTO {
    private Integer id;
    private Integer reporterId;  // 신고자 (User)의 ID
    private Integer reportedId;  // 신고된 사람 (User)의 ID
    private Integer categoryId;  // 카테고리 (ReportCategory)의 ID
    private Integer togetherId;  // Together의 ID
}
