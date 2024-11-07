package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    // 제목에서 키워드 포함
    List<Notice> findByTitleContaining(String keyword);

    // 제목과 내용에 하나의 키워드를 포함하여 검색
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword%")
    List<Notice> findByTitleOrContentContaining(String keyword);

    // 작성일 기준으로 오름차순 정렬
    List<Notice> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Notice> findByOrderByCreatedAtDesc();
}
