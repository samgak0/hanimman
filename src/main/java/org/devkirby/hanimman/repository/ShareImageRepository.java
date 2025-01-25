package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareImageRepository extends JpaRepository<ShareImage, Integer> {
    List<ShareImage> findByParentId(Integer parentId);

    // 삭제되지 않은 이미지 조회
    List<ShareImage> findByDeletedAtIsNull();

    // 특정 parent에 대해 deletedAt이 null인 이미지들만 조회
    List<ShareImage> findByParentAndDeletedAtIsNull(Share parent);

    List<ShareImage> findAllByParentId(Integer shareId);
}
