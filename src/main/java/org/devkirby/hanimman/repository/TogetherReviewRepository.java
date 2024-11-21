package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherReviewRepository extends JpaRepository<TogetherReview, Integer> {
    Page<TogetherReview> findByUserId(Integer userId, Pageable pageable);
}
