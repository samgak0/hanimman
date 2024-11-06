package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.TogetherParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherParticipantsRepository extends JpaRepository<TogetherParticipants, Integer> {
    List<TogetherParticipants> findByUserId(Integer userId);

    List<TogetherParticipants> findByParent(Integer parent);

    // 거절하지 않은 사람들 조회
    List<TogetherParticipants> findByParentIdAndRejectedFalse(Integer parentId);
}
