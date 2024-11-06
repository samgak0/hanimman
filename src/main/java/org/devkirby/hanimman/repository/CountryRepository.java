package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findByCityId(Integer cityId);


}
