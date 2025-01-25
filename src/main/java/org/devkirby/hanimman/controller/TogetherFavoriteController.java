package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.service.TogetherFavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/together-favorite")
@RequiredArgsConstructor
public class TogetherFavoriteController {
    private final TogetherFavoriteService togetherFavoriteService;
    private final Logger log = LoggerFactory.getLogger(TogetherController.class);

    @PostMapping("/create")
    public void createTogetherFavorite(@RequestBody TogetherDTO togetherDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        log.info("같이가요 좋아요");
        log.info("찜 유저 ID : " + loginUser.getId() + " 찜 글 ID : " + togetherDTO.getId());
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
