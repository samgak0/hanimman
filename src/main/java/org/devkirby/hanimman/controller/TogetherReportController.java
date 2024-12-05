package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.entity.TogetherReport;
import org.devkirby.hanimman.service.TogetherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> report(@Valid @RequestBody TogetherReportDTO togetherReportDTO) {
        togetherReportService.create(togetherReportDTO); // 서비스 메서드 호출
        return ResponseEntity.ok().build(); // 200 OK 응답 반환
    }

    //조회
    @GetMapping("/select-reports")
    public ResponseEntity<List<TogetherReport>> getSelectReports() {
        List<TogetherReport> selectReports = togetherReportService.findAllSelectReports();
        return ResponseEntity.ok(selectReports);

    }
}
