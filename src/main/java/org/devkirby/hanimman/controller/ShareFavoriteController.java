package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.service.ShareFavoriteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/share-favorite")
@RequiredArgsConstructor
public class ShareFavoriteController {
    private final ShareFavoriteService shareFavoriteService;;

    @PostMapping("/create")
    public void createShareFavorite(@RequestBody ShareDTO shareDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareFavoriteService.create(loginUser.getId(), shareDTO.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteShareFavorite(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareFavoriteService.delete(id, loginUser.getId());
    }

    @GetMapping("/count/{parentId}")
    public int countShareFavorites(@PathVariable Integer parentId) {
        return shareFavoriteService.countByParentId(parentId);
    }
}
