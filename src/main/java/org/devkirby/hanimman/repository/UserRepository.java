package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
