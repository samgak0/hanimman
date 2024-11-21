package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareReviewDTO;
import org.devkirby.hanimman.service.ShareReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/share-review")
@RequiredArgsConstructor
public class ShareReviewController {
    private final ShareReviewService shareReviewService;

    @PostMapping
    public Map<String, Object> createReview(ShareReviewDTO shareReviewDTO) {
        if(shareReviewDTO.getContent().length() >100){
            throw new IllegalArgumentException("후기 내용은 100자 이하로 작성해주세요.");
        }else if(shareReviewDTO.getRating() < -2 || shareReviewDTO.getRating() > 2){
            throw new IllegalArgumentException("평점은 1점 이상 5점 이하로 작성해주세요.");
        }
        shareReviewService.createReview(shareReviewDTO);
        return Map.of("code", 200, "msg", "후기 작성에 성공했습니다.");
    }

    @GetMapping("/{id}")
    public ShareReviewDTO readReview(Integer id) {
        return shareReviewService.readReview(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateReview(Integer id, ShareReviewDTO shareReviewDTO) {
        shareReviewDTO.setId(id);
        shareReviewService.updateReview(shareReviewDTO);
        return Map.of("code", 200, "msg", "후기 수정에 성공했습니다.");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReview(Integer id) {
        shareReviewService.deleteReview(id);
        return Map.of("code", 200, "msg", "후기 삭제에 성공했습니다.");
    }

    @GetMapping("/written/{userId}")
    public Page<ShareReviewDTO> getWrittenReviews(
            @PageableDefault(size = 10)Pageable pageable, Integer userId) {
        return shareReviewService.getWrittenReviews(userId, pageable);
    }

    @GetMapping("/accept/{targetId}")
    public Page<ShareReviewDTO> getAcceptReviews(
            @PageableDefault(size = 10)Pageable pageable, Integer targetId) {
        return shareReviewService.getAcceptReviews(targetId, pageable);
    }
}
