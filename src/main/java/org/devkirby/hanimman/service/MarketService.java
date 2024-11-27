package org.devkirby.hanimman.service;

import jakarta.persistence.EntityNotFoundException;
import org.devkirby.hanimman.dto.MarketDTO;
import org.devkirby.hanimman.entity.Market;
import org.devkirby.hanimman.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    public MarketDTO getMarketById(Integer id) {
        // 마켓 객체를 직접 반환
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("마켓종류 : " + id));

        // MarketDTO로 변환하여 반환
        return MarketDTO.builder()
                .id(market.getId())
                .category(market.getCategory().getId()) // 카테고리 ID를 가져옵니다.
                .name(market.getName())
                .latitude(market.getLatitude())
                .longitude(market.getLongitude())
                .cityCode(market.getAddress().getCityCode())
                .districtCode(market.getAddress().getDistrictCode())
                .neighborhoodCode(market.getAddress().getNeighborhoodCode())
                .address(market.getAddressDetail())
                .build();
        }

    // 카테고리 ID로 마켓 조회
    public List<MarketDTO> getMarketsByCategoryId(Integer categoryId) {
        List<Market> markets = marketRepository.findByCategoryId(categoryId);
        return markets.stream()
                .map(market -> MarketDTO.builder()
                        .id(market.getId())
                        .category(market.getCategory().getId())
                        .name(market.getName())
                        .latitude(market.getLatitude())
                        .longitude(market.getLongitude())
                        .cityCode(market.getAddress().getCityCode())
                        .districtCode(market.getAddress().getDistrictCode())
                        .neighborhoodCode(market.getAddress().getNeighborhoodCode())
                        .address(market.getAddressDetail())
                        .build())
                .collect(Collectors.toList());
    }
}
