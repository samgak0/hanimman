package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Integer> {
    
}
