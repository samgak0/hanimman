package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Notices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticesRepository extends JpaRepository<Notices, Integer> {
    // 제목에서 키워드 포함
    @Query("SELECT s FROM notices s WHERE s.title LIKE %:keyword%")
    List<Notices> findByTitleContaining(String keyword);

    // 제목 및 내용에 키워드 포함
    @Query("SELECT s FROM notices s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword%")
    List<Notices> findByTitleOrContentContaining(String keyword);

    // 작성일 기준으로 오름차순 정렬
    List<Notices> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Notices> findByOrderByCreatedAtDesc();
}
