package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.TogetherLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TogetherLocationRepository extends JpaRepository<TogetherLocation, Integer> {
}
