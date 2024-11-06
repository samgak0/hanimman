package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareFavorites;
import org.devkirby.hanimman.entity.TogetherFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherFavoritesRepository extends JpaRepository<TogetherFavorites, Integer> {
    List<TogetherFavorites> findByUserId(Integer userId);

    List<TogetherFavorites> findByParent(Integer parent);
}
