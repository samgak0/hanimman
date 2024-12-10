package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {

    List<Market> findByCategoryId(int categoryId);

    List<Market> findByAddress_CityCodeAndCategory_Id(String cityCode, Integer categoryId);

    Market findByCategoryIdAndName(Integer categoryId, String name);
}
