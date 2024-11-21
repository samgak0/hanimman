package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareReviewRepository extends JpaRepository<ShareReview, Integer> {
    Page<ShareReview> findByUserIdAndDeletedAtIsNull(Integer userId, Pageable pageable);
    Page<ShareReview> findByTargetIdAndDeletedAtIsNull(Integer targetId, Pageable pageable);

    ShareReview findByUserIdAndTargetIdAndParentIdAndDeletedAtIsNull(Integer userId, Integer targetId, Integer parentId);
}
