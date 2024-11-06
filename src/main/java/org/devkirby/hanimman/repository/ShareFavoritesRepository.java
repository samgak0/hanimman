package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareFavoritesRepository extends JpaRepository<ShareFavorites, Integer> {
    List<ShareFavorites> findByUserId(Integer userId);

    List<ShareFavorites> findByParent(Integer parent);
}
