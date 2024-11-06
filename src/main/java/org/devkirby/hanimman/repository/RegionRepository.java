package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Country;
import org.devkirby.hanimman.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

}
