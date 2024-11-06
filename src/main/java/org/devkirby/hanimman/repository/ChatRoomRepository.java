package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

}
