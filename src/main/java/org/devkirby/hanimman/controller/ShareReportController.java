package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ReportCategoryDTO;
import org.devkirby.hanimman.dto.ShareReportDTO;
import org.devkirby.hanimman.service.ShareReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/share-report")
public class ShareReportController {
    @Autowired
    private ShareReportService shareReportService;

    @PostMapping("/report")
    public ResponseEntity<Void> report(@Valid @RequestBody ShareReportDTO shareReportDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareReportDTO.setReporterId(loginUser.getId());
        shareReportService.create(shareReportDTO);
        return ResponseEntity.ok().build();
    }

    // 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<ReportCategoryDTO>> getCategories() {
        List<ReportCategoryDTO> categories = shareReportService.findAllCategories();
        return ResponseEntity.ok(categories);
    }
}
