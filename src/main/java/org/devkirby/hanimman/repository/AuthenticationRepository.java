package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Authentication;
import org.devkirby.hanimman.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {

    List<Region> findByCountryId(Integer countryId);
}
