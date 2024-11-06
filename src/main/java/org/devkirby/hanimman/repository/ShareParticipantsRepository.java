package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareParticipantsRepository extends JpaRepository<ShareParticipants, Integer> {
    List<ShareParticipants> findByUserId(Integer userId);

    List<ShareParticipants> findByParent(Integer parent);

    // 거절하지 않은 사람들 조회
    List<ShareParticipants> findByParentIdAndRejectedFalse(Integer parentId);
}
