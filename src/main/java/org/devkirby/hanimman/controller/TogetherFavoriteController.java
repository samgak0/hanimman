package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.service.TogetherFavoriteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/together-favorite")
@RequiredArgsConstructor
public class TogetherFavoriteController {
    private final TogetherFavoriteService togetherFavoriteService;

    @PostMapping("/create")
    public void createTogetherFavorite(@RequestBody TogetherDTO togetherDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        togetherFavoriteService.create(loginUser.getId(), togetherDTO.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteTogetherFavorite(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        togetherFavoriteService.delete(id, loginUser.getId());
    }

    @GetMapping("/count/{parentId}")
    public int countTogetherFavorites(@PathVariable Integer parentId) {
        return togetherFavoriteService.countByParentId(parentId);
    }
}
