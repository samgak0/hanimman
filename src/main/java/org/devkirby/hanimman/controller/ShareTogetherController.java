package org.devkirby.hanimman.controller;

import java.util.List;

import org.devkirby.hanimman.dto.ShareTogetherDTO;
import org.devkirby.hanimman.service.ShareTogetherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shares-togethers")
public class ShareTogetherController {

    private final ShareTogetherService service;

    /**
     * 나눠요와 함께해요 게시글 목록 조회
     * 
     * @return 나눠요와 함께해요 게시글 목록
     */
    @GetMapping
    public List<ShareTogetherDTO> getSharesAndTogethers() {
        return service.getSharesAndTogethers();
    }
}