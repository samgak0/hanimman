package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherRepository extends JpaRepository<Together, Integer> {

    // 특정 사용자 ID에 해당하는 Together 리스트 조회
    List<Together> findByUserIdAndDeletedAtIsNull(Integer userId);

    // 제목에서 키워드 포함
    List<Together> findByTitleContainingAndDeletedAtIsNull(String keyword);

    // 제목과 내용에 키워드 포함
    List<Together> findByTitleContainingOrContentContainingAndDeletedAtIsNull(String titleKeyword, String contentKeyword);

    // 품목에 키워드 포함
    List<Together> findByItemContainingAndDeletedAtIsNull(String keyword);
}
