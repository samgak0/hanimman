package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareParticipant;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareParticipantRepository extends JpaRepository<ShareParticipant, Integer> {
    List<ShareParticipant> findByUserId(Integer userId);

    List<ShareParticipant> findByParent(Together parent);

    List<ShareParticipant> findByParentAndRejectedFalse(Together parent);
}
