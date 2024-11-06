package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Integer> {
    List<Share> findByUserId(Integer userId);
}
