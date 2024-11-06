package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareReview;
import org.devkirby.hanimman.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareReviewRepository extends JpaRepository<ShareReview, Integer> {

    List<ShareReview> findByUserId(Integer userId);

    List<ShareReview> findByParent(Share parent);
}
