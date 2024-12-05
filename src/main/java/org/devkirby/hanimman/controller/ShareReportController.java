package org.devkirby.hanimman.controller;

import jakarta.validation.Valid;
import org.devkirby.hanimman.dto.ShareReportDTO;
import org.devkirby.hanimman.service.ShareReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share-report")
public class ShareReportController {
    @Autowired
    private ShareReportService shareReportService;

    @PostMapping
    public ResponseEntity<Void> report(@Valid @RequestBody ShareReportDTO shareReportDTO) {
        shareReportService.create(shareReportDTO);
        return ResponseEntity.ok().build();
    }
}
