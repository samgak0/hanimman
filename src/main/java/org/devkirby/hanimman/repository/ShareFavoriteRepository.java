package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareFavoriteRepository extends JpaRepository<ShareFavorite, Integer> {
    List<ShareFavorite> findByUserId(Integer userId);
    List<ShareFavorite> findByParent(Integer parent);
}
