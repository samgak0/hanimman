package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.ShareLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareLocationRepository extends JpaRepository<ShareLocation, Integer> {
}
