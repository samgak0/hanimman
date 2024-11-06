package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.devkirby.hanimman.entity.TogetherImage;

@Repository
public interface TogetherImagesRepository extends JpaRepository<TogetherImage, Integer> {
    List<TogetherImage> findByParentId(Integer parentId);

    // 삭제되지 않은 이미지 조회
    List<TogetherImage> findByDeletedAtIsNull();

    // 특정 parent에 대해 deletedAt이 null인 이미지들만 조회
    List<TogetherImage> findByParentAndDeletedAtIsNull(Together parent);
}
