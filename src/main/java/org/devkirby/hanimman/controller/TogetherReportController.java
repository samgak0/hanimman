package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.entity.ReportCategory;
import org.devkirby.hanimman.entity.TogetherReport;
import org.devkirby.hanimman.service.TogetherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/together-report")
public class TogetherReportController {
    @Autowired
    private TogetherReportService togetherReportService;

    //생성
    @PostMapping("/report")
    public ResponseEntity<Void> report(@Valid @RequestBody TogetherReportDTO togetherReportDTO,
                                       @AuthenticationPrincipal CustomUserDetails loginUser) {
        togetherReportDTO.setReporterId(loginUser.getId()); // 신고자 ID 설정
        togetherReportService.create(togetherReportDTO); // 서비스 메서드 호출
        return ResponseEntity.ok().build(); // 200 OK 응답 반환
    }

    //조회
    @GetMapping("/select-reports")
    public ResponseEntity<List<TogetherReport>> getSelectReports() {
        List<TogetherReport> selectReports = togetherReportService.findAllSelectReports();
        return ResponseEntity.ok(selectReports);

    }

    // 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<ReportCategoryDTO>> getCategories() {
        List<ReportCategoryDTO> categories = togetherReportService.findAllCategories();
        return ResponseEntity.ok(categories);
    }
}
