package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Integer> {
    
}
