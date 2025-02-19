package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.entity.TogetherReport;
import org.devkirby.hanimman.service.TogetherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/together-report")
public class TogetherReportController {
    @Autowired
    private TogetherReportService togetherReportService;

    /**
     * 같이가요 게시글 신고
     * 
     * @param togetherReportDTO 신고 정보
     * @param loginUser         로그인 유저
     * @return 신고 결과
     */
    @PostMapping("/report")
    public ResponseEntity<Void> report(@Valid @RequestBody TogetherReportDTO togetherReportDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        togetherReportDTO.setReporterId(loginUser.getId()); // 신고자 ID 설정
        togetherReportService.create(togetherReportDTO); // 서비스 메서드 호출
        return ResponseEntity.ok().build(); // 200 OK 응답 반환
    }

    /**
     * 선택 신고 조회
     * 
     * @return 선택 신고 조회 결과
     */
    @GetMapping("/select-reports")
    public ResponseEntity<List<TogetherReport>> getSelectReports() {
        List<TogetherReport> selectReports = togetherReportService.findAllSelectReports();
        return ResponseEntity.ok(selectReports);

    }

    /**
     * 카테고리 조회
     * 
     * @return 카테고리 조회 결과
     */
    @GetMapping("/categories")
    public ResponseEntity<List<ReportCategoryDTO>> getCategories() {
        List<ReportCategoryDTO> categories = togetherReportService.findAllCategories();
        return ResponseEntity.ok(categories);
    }
}
