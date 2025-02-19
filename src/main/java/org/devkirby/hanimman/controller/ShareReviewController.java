package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ShareReviewDTO;
import org.devkirby.hanimman.service.ShareReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/share-review")
@RequiredArgsConstructor
public class ShareReviewController {
    private final ShareReviewService shareReviewService;
    private static final String ERROR_CONTENT_LENGTH = "후기 내용은 100자 이하로 작성해주세요.";
    private static final String ERROR_RATING = "평점은 1점 이상 5점 이하로 작성해주세요.";

    /**
     * 나눠요 게시글 후기 작성
     */
    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ShareReviewDTO shareReviewDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        validateReview(shareReviewDTO);
        shareReviewDTO.setUserId(loginUser.getId());
        shareReviewService.createReview(shareReviewDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 나눠요 게시글 후기 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShareReviewDTO> readReview(@PathVariable Integer id) {
        return ResponseEntity.ok(shareReviewService.readReview(id));
    }

    /**
     * 나눠요 게시글 후기 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable Integer id,
            @RequestBody ShareReviewDTO shareReviewDTO,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        validateReview(shareReviewDTO);
        shareReviewDTO.setId(id);
        shareReviewDTO.setUserId(loginUser.getId());
        shareReviewService.updateReview(shareReviewDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 나눠요 게시글 후기 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        shareReviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 작성한 후기 목록 조회
     */
    @GetMapping("/written/{userId}")
    public ResponseEntity<Page<ShareReviewDTO>> getWrittenReviews(
            @PageableDefault(size = 10) Pageable pageable,
            @PathVariable Integer userId) {
        return ResponseEntity.ok(shareReviewService.getWrittenReviews(userId, pageable));
    }

    /**
     * 받은 후기 목록 조회
     */
    @GetMapping("/received/{targetId}")
    public ResponseEntity<Page<ShareReviewDTO>> getReceivedReviews(
            @PageableDefault(size = 10) Pageable pageable,
            @PathVariable Integer targetId) {
        return ResponseEntity.ok(shareReviewService.getAcceptReviews(targetId, pageable));
    }

    private void validateReview(ShareReviewDTO review) {
        if (review.getContent().length() > 100) {
            throw new IllegalArgumentException(ERROR_CONTENT_LENGTH);
        }
        if (review.getRating() < -2 || review.getRating() > 2) {
            throw new IllegalArgumentException(ERROR_RATING);
        }
    }
}
