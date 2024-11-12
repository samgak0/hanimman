package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareFavoriteDTO;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.ShareFavoriteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/share-favorite")
@RequiredArgsConstructor
public class ShareFavoriteController {
    private final ShareFavoriteService shareFavoriteService;;

    @PostMapping
    public void createShareFavorite(@RequestBody ShareDTO shareDTO, @AuthenticationPrincipal UserDTO loginUser) {
        shareFavoriteService.create(shareDTO.getId(), loginUser.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteShareFavorite(@PathVariable Integer id) {
        shareFavoriteService.delete(id);
    }

    @GetMapping("/count/{parentId}")
    public int countShareFavorites(@PathVariable Integer parentId) {
        return shareFavoriteService.countByParentId(parentId);
    }
}
