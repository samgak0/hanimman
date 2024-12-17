package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherReviewDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.devkirby.hanimman.service.TogetherReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/together-review")
@RequiredArgsConstructor
public class TogetherReviewController {
    private static final Logger log = LoggerFactory.getLogger(TogetherReviewController.class);
    private final TogetherReviewService togetherReviewService;
    @Autowired
    private TogetherRepository togetherRepository;

    @PostMapping("/create")
    public Map<String, Object> createReview(@RequestBody  TogetherReviewDTO togetherReviewDTO,
                                            @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (togetherReviewDTO.getContent().length() > 100) {
            throw new IllegalArgumentException("후기 내용은 100자 이하로 작성해주세요.");
        } else if (togetherReviewDTO.getRating() < -2 || togetherReviewDTO.getRating() > 2) {
            throw new IllegalArgumentException("평점은 1점 이상 5점 이하로 작성해주세요.");
        }
        togetherReviewDTO.setUserId(loginUser.getId());
        togetherReviewDTO.setCreatedAt(Instant.now());
        Optional<Together> together = togetherRepository.findById(togetherReviewDTO.getParentId());
        if(loginUser.getId() == together.get().getUser().getId()){

        }else{
            togetherReviewDTO.setTargetId(together.get().getUser().getId());
        }
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
