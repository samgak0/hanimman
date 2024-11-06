package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    // 제목에서 키워드 포함
    @Query("SELECT s FROM inquiries s WHERE s.title LIKE %:keyword%")
    List<Inquiry> findByTitleContaining(String keyword);

    // 제목 및 내용에 키워드 포함
    @Query("SELECT s FROM inquiries s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword%")
    List<Inquiry> findByTitleOrContentContaining(String keyword);

    // 작성일 기준으로 오름차순 정렬
    List<Inquiry> findByOrderByCreatedAtAsc();

    // 작성일 기준으로 내림차순 정렬
    List<Inquiry> findByOrderByCreatedAtDesc();
}
