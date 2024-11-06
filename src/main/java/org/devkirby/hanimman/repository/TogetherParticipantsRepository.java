package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.TogetherParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherParticipantsRepository extends JpaRepository<TogetherParticipant, Integer> {
    List<TogetherParticipant> findByUserId(Integer userId);

    List<TogetherParticipant> findByParent(Integer parent);

    // 거절하지 않은 사람들 조회
    List<TogetherParticipant> findByParentIdAndRejectedFalse(Integer parentId);
}
