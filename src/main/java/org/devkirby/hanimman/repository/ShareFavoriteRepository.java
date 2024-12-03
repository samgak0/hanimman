package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareFavorite;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareFavoriteRepository extends JpaRepository<ShareFavorite, Integer> {
    List<ShareFavorite> findByUserId(Integer userId);
    List<ShareFavorite> findByParent(Share parent);
    int countByParent(Share parent);
    ShareFavorite findByUserAndParent(User user, Share parent);
    boolean existsByUserAndParent(User user, Share parent);
}
