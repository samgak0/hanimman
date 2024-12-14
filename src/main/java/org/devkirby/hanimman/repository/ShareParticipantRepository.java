package org.devkirby.hanimman.repository;

import java.util.List;

import org.devkirby.hanimman.entity.ShareParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareParticipantRepository extends JpaRepository<ShareParticipant, Integer> {
    List<ShareParticipant> findByUserId(Integer userId);

    List<ShareParticipant> findByParentId(Integer parentId);

    List<ShareParticipant> findByParentIdAndRejectedIsNull(Integer parentId);

    ShareParticipant findByUserIdAndParentId(Integer userId, Integer parentId);

    Boolean existsByUserIdAndParentId(Integer userId, Integer parentId);

    List<ShareParticipant> findByUserIdAndRejectedIsNull(Integer userId);

    Integer countByParentId(Integer parentId);
}
