package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.service.ShareImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/shareImage")
public class ShareImageController {
    @Autowired
    private ShareImageService shareImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("shareId") Integer shareId
    ) throws IOException {
        String result = shareImageService.uploadImage(file, shareId);
        return ResponseEntity.ok(result);
    }
}