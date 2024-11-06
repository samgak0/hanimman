package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    // 제목에서 키워드 포함
    List<Faq> findByTitleContaining(String keyword);

    // 제목과 내용에 키워드 포함
    List<Faq> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 작성일 기준으로 오름차순 정렬
    List<Faq> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Faq> findByOrderByCreatedAtDesc();

}
