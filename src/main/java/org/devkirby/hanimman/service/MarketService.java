package org.devkirby.hanimman.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.MarketDTO;
import org.devkirby.hanimman.entity.Market;
import org.devkirby.hanimman.repository.MarketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketService {
    // MarketRepository를 주입받아 데이터베이스 상의 마켓 정보를 조회합니다.
    @Autowired
    private MarketRepository marketRepository;

    private final ModelMapper modelMapper;

    /**
     * 주어진 ID로 마켓을 조회합니다.
     */
    public MarketDTO getMarketById(Integer id) {
        // ID로 마켓을 조회하고, 없다면 예외를 발생시킵니다.
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("마켓을 찾을 수 없습니다: ID " + id));

        // 조회된 마켓을 DTO로 변환하여 반환합니다.
        return convertToDTO(market);
    }

    /**
     * 주어진 카테고리 ID로 마켓 목록을 조회합니다.
     * @param categoryId 조회할 마켓의 카테고리 ID
     * @return 해당 카테고리의 마켓 정보를 담고 있는 MarketDTO 리스트
     */
    public List<MarketDTO> getMarketsByCategoryId(Integer categoryId) {
        // 카테고리 ID로 마켓 목록을 조회합니다.
        List<Market> markets = marketRepository.findByCategoryId(categoryId);
        // 조회된 마켓을 DTO로 변환하여 리스트로 반환합니다.
        return markets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MarketDTO> getMarketsByAddressAndCategory(Integer categoryId, String cityCode) {
        // City ID와 카테고리 ID로 마켓 목록을 조회합니다.
        List<Market> markets = marketRepository.findByAddress_CityCodeAndCategory_Id(cityCode, categoryId);
        // 조회된 마켓을 DTO로 변환하여 리스트로 반환합니다.
        return markets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MarketDTO getMarketByCategoryIdAndName(Integer categoryId, String name){
        Market market =  marketRepository.findByCategoryIdAndName(categoryId, name);
        if (market == null) {
            throw new EntityNotFoundException("해당 카테고리 ID와 이름을 가진 마켓을 찾을 수 없습니다: 카테고리 ID " + categoryId + ", 이름 " + name);
        }
        MarketDTO marketDTO = convertToDTO(market);
        return marketDTO;
    }


    /**
     * Market 엔티티를 MarketDTO로 변환합니다.
     */
    private MarketDTO convertToDTO(Market market) {
        // Market 엔티티의 데이터를 사용하여 MarketDTO 객체를 생성합니다.
        return MarketDTO.builder()
                .id(market.getId())
                .category(market.getCategory().getId()) // 카테고리 ID
                .name(market.getName()) // 마켓 이름
                .latitude(market.getLatitude()) // 위도
                .longitude(market.getLongitude()) // 경도
                .cityCode(market.getAddress().getCityCode()) // 도시 코드
                .districtCode(market.getAddress().getDistrictCode()) // 구 코드
                .neighborhoodCode(market.getAddress().getNeighborhoodCode()) // 동 코드
                .addressDetail(market.getAddressDetail()) // 주소 상세 정보
                .addressId(market.getAddress().getId()) // 주소 ID
                .build(); // DTO 객체 반환
    }
}