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
    private final ShareFavoriteService shareFavoriteService;

    /**
     * 나눠요 게시글 즐겨찾기 생성
     * 
     * @param shareDTO  나눠요 게시글 정보
     * @param loginUser 로그인 유저
     */
    @PostMapping("/create")
    public void createShareFavorite(@RequestBody ShareDTO shareDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareFavoriteService.create(loginUser.getId(), shareDTO.getId());
    }

    /**
     * 나눠요 게시글 즐겨찾기 삭제
     * 
     * @param id        나눠요 게시글 아이디
     * @param loginUser 로그인 유저
     */
    @DeleteMapping("/{id}")
    public void deleteShareFavorite(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareFavoriteService.delete(id, loginUser.getId());
    }

    /**
     * 나눠요 게시글 즐겨찾기 개수 조회
     * 
     * @param parentId 나눠요 게시글 아이디
     * @return 즐겨찾기 개수
     */
    @GetMapping("/count/{parentId}")
    public int countShareFavorites(@PathVariable Integer parentId) {
        return shareFavoriteService.countByParentId(parentId);
    }
}
