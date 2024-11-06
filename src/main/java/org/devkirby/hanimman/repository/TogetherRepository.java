package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherRepository extends JpaRepository<Together, Integer> {
    List<Together> findByUserId(Integer userId);

    // 제목에서 키워드 포함
    @Query("SELECT s FROM together s WHERE s.title LIKE %:keyword%")
    List<Together> findByTitleContaining(String keyword);

    // 제목 및 내용에 키워드 포함
    @Query("SELECT s FROM together s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword%")
    List<Together> findByTitleOrContentContaining(String keyword);

    // 품목에 키워드 포함
    @Query("SELECT s FROM together s WHERE s.item LIKE %:keyword%")
    List<Together> findByItemContaining(String keyword);
}
