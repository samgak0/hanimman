package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

    /**
     * 메인 페이지 조회
     * 
     * @param loginUser 로그인 유저
     */
    @GetMapping
    public void listAll(@AuthenticationPrincipal CustomUserDetails loginUser) {

    }
}
