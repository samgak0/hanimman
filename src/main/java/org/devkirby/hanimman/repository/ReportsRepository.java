package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Integer> {
    // 제목에서 키워드 포함
    List<Reports> findByTitleContaining(String keyword);

    // 제목 및 내용에 키워드 포함
    List<Reports> findByTitleOrContentContaining(String keyword);

    // 작성일 기준으로 오름차순 정렬
    List<Reports> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Reports> findByOrderByCreatedAtDesc();
}
