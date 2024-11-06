package org.devkirby.hanimman.repository;

import java.util.List;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareParticipant;
import org.devkirby.hanimman.entity.Together;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareParticipantRepository extends JpaRepository<ShareParticipant, Integer> {
    List<ShareParticipant> findByUserId(Integer userId);

    List<ShareParticipant> findByParent(Share parent);

    List<ShareParticipant> findByParentAndRejectedFalse(Share parent);
}
