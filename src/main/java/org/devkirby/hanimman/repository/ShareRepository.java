package org.devkirby.hanimman.repository;

import java.util.List;

import org.devkirby.hanimman.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, Integer> {
    // 작성자 ID로 검색
    List<Share> findByUserIdAndDeletedAtIsNull(Integer userId);

    // 제목에서 키워드 포함
    List<Share> findByTitleContainingAndDeletedAtIsNull(String keyword);

    // 제목 및 내용에 키워드 포함
    List<Share> findByTitleOrContentContainingAndDeletedAtIsNull(String titleKeyword, String contentKeyword);

    // 품목에 키워드 포함
    List<Share> findByItemContainingAndDeletedAtIsNull(String keyword);

    Page<Share> findByTitleContainingOrContentContainingAndDeletedAtIsNull(String titleKeyword, String contentKeyword, Pageable pageable);

    Page<Share> findByIsEndIsFalse(Pageable pageable);
}