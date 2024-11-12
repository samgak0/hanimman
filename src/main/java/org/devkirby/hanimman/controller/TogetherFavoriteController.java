package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.service.TogetherFavoriteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/together-favorite")
@RequiredArgsConstructor
public class TogetherFavoriteController {
    private final TogetherFavoriteService togetherFavoriteService;

    @PostMapping
    public void createTogetherFavorite(@RequestBody TogetherDTO togetherDTO, @AuthenticationPrincipal UserDTO loginUser) {
        togetherFavoriteService.create(togetherDTO.getId(), loginUser.getId());
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
