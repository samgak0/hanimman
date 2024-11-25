package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.dto.TogetherReportDTO;
import org.devkirby.hanimman.service.TogetherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/together-report")
public class TogetherReportController {
    @Autowired
    private TogetherReportService togetherReportService;

    @PostMapping
    public ResponseEntity<Void> report(@Valid @RequestBody TogetherReportDTO reportDTO) {
        togetherReportService.create(reportDTO); // 서비스 메서드 호출
        return ResponseEntity.ok().build(); // 200 OK 응답 반환
    }
}
