package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TogetherReviewService {
    void createReview(TogetherReviewDTO togetherReviewDTO);
    TogetherReviewDTO readReview(Integer id);
    void updateReview(TogetherReviewDTO togetherReviewDTO);
    void deleteReview(Integer id);

    Page<TogetherReviewDTO> getWrittenReviews(Integer userId, Pageable page);
    Page<TogetherReviewDTO> getAcceptReviews(Integer targetId, Pageable page);
}
