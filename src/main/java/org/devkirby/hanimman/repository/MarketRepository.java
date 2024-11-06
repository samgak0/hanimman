package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {

}
