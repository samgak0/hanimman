package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Integer> {

}
