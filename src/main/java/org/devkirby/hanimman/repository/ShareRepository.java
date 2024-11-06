package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRepository extends JpaRepository<Share, Integer> {
    List<Share> findByUserId(Integer userId);

    // 제목에서 키워드 포함
    List<Share> findByTitleContaining(String keyword);

    // 제목 및 내용에 키워드 포함
    List<Share> findByTitleOrContentContaining(String keyword);

    // 품목에 키워드 포함
    List<Share> findByItemContaining(String keyword);
}