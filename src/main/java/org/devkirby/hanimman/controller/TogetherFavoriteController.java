package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.UserDTO;
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
    public void createTogetherFavorite(@RequestBody Integer parentId, @AuthenticationPrincipal CustomUserDetails loginUser) {
        log.info("찜 유저 ID : " + loginUser.getId() +" 찜 글 ID : " + parentId);
        togetherFavoriteService.create(loginUser.getId(), parentId);
    }

    @DeleteMapping("/{id}")
    public void deleteTogetherFavorite(@PathVariable Integer id) {
        togetherFavoriteService.delete(id);
    }

    @GetMapping("/count/{parentId}")
    public int countTogetherFavorites(@PathVariable Integer parentId) {
        return togetherFavoriteService.countByParentId(parentId);
    }
}
