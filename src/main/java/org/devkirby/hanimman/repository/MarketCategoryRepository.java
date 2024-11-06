package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.MarketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketCategoryRepository extends JpaRepository<MarketCategory, Integer> {
}
