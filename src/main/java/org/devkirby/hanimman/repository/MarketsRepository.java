package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Markets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketsRepository extends JpaRepository<Markets, Integer> {

}
