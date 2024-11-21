package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherReviewDTO;
import org.devkirby.hanimman.service.TogetherReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/together-review")
@RequiredArgsConstructor
public class TogetherReviewController {
    private final TogetherReviewService togetherReviewService;

    @PostMapping
    public Map<String, Object> createReview(TogetherReviewDTO togetherReviewDTO) {
        togetherReviewService.createReview(togetherReviewDTO);
        return Map.of("code", 200, "msg", "후기 작성에 성공했습니다.");
    }

    @GetMapping("/{id}")
    public TogetherReviewDTO readReview(Integer id) {
        return togetherReviewService.readReview(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateReview(Integer id, TogetherReviewDTO togetherReviewDTO) {
        togetherReviewDTO.setId(id);
        togetherReviewService.updateReview(togetherReviewDTO);
        return Map.of("code", 200, "msg", "후기 수정에 성공했습니다.");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReview(Integer id) {
        togetherReviewService.deleteReview(id);
        return Map.of("code", 200, "msg", "후기 삭제에 성공했습니다.");
    }

    @GetMapping("/written/{userId}")
    public Page<TogetherReviewDTO> getWrittenReviews(
            @PageableDefault(size = 10) Pageable pageable, Integer userId) {
        return togetherReviewService.getWrittenReviews(userId, pageable);
    }

    @GetMapping("/accept/{targetId}")
    public Page<TogetherReviewDTO> getAcceptReviews(
            @PageableDefault(size = 10) Pageable pageable, Integer targetId) {
        return togetherReviewService.getAcceptReviews(targetId, pageable);
    }
}
