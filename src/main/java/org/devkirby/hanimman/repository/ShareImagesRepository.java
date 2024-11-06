package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareImagesRepository extends JpaRepository<ShareImages, Integer> {
    List<ShareImages> findByParentId(Integer parentId);

    // 삭제되지 않은 이미지 조회
    List<ShareImages> findByDeletedAtIsNull();
}
