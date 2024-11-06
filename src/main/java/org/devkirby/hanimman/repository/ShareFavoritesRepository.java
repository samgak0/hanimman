package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareFavorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareFavoritesRepository extends JpaRepository<ShareFavorites, Integer> {
    List<ShareFavorites> findByUserId(Integer userId);

    List<ShareFavorites> findByParent(Integer parent);
}
