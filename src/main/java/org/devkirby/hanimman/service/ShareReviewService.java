package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShareReviewService {
    void createReview(ShareReviewDTO shareReviewDTO);
    ShareReviewDTO readReview(Integer id);
    void updateReview(ShareReviewDTO shareReviewDTO);
    void deleteReview(Integer id);

    Page<ShareReviewDTO> getWrittenReviews(Integer userId, Pageable page);
    Page<ShareReviewDTO> getMyReviews(Integer targetId, Pageable page);
}
