package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.TogetherReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TogetherReviewRepository extends JpaRepository<TogetherReview, Integer> {
    Page<TogetherReview> findByUserIdAndDeletedAtIsNull(Integer userId, Pageable pageable);
    Page<TogetherReview> findByTargetIdAndDeletedAtIsNull(Integer targetId, Pageable pageable);

    Optional<TogetherReview> findByUserIdAndTargetIdAndParentIdAndDeletedAtIsNull
            (Integer userId, Integer targetId, Integer parentId);
}
