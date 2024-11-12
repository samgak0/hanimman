package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    // 제목에서 키워드 포함
    List<Faq> findByTitleContaining(String keyword);

    // 제목과 내용에 키워드 포함
    @Query("SELECT f FROM Faq f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword%")
    List<Faq> findByTitleOrContentContaining(String keyword);

    // 작성일 기준으로 오름차순 정렬
    List<Faq> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Faq> findByOrderByCreatedAtDesc();

    Page<Faq> findByTitleContainingOrContentContainingAndDeletedAtIsNull(String titleKeyword, String contentKeyword, Pageable pageable);
}
