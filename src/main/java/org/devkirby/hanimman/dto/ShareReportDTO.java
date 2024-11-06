package org.devkirby.hanimman.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareReportDTO {
    private Integer id;
    private Integer reporterId;  // Reporter의 ID만 전달
    private Integer reportedId;  // Reported의 ID만 전달
    private Integer categoryId;  // ReportCategory의 ID만 전달
    private Integer shareId;     // Share의 ID만 전달
}
