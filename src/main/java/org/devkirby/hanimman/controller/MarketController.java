package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.dto.MarketDTO;
import org.devkirby.hanimman.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class MarketController {

    @Autowired
    private MarketService marketService;

    /**
     * ID로 마켓 조회
     * 
     * @param id 마켓 아이디
     * @return 마켓 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarketDTO> getMarketById(@PathVariable Integer id) {
        MarketDTO marketDTO = marketService.getMarketById(id);
        // System.out.println(marketDTO);
        return ResponseEntity.ok(marketDTO); // 200 OK 반환
    }

    /**
     * 카테고리로 마켓 조회
     * 
     * @param categoryId 카테고리 아이디
     * @return 마켓 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MarketDTO>> getMarketsByCategory(@PathVariable Integer categoryId) {
        List<MarketDTO> markets = marketService.getMarketsByCategoryId(categoryId);
        System.out.println(markets);
        return ResponseEntity.ok(markets);
    }

    /**
     * city ID와 카테고리로 주소 조회
     * 
     * @param categoryId 카테고리 아이디
     * @param cityCode   도시 코드
     * @return 마켓 목록
     */
    @GetMapping("/category/{categoryId}/citycode/{cityCode}")
    public ResponseEntity<List<MarketDTO>> getMarketsByCategoryAndCity(@PathVariable Integer categoryId,
            @PathVariable String cityCode) {
        List<MarketDTO> markets = marketService.getMarketsByAddressAndCategory(categoryId, cityCode);
        System.out.println(markets);
        return ResponseEntity.ok(markets);
    }

    /**
     * 카테고리 ID와 이름으로 마켓 조회
     * 
     * @param categoryId 카테고리 아이디
     * @param name       마켓 이름
     * @return 마켓 정보
     */
    @GetMapping("/category/{categoryId}/{name}")
    public ResponseEntity<MarketDTO> getMarketByCategoryIdAndName(@PathVariable Integer categoryId,
            @PathVariable String name) {
        MarketDTO marketDTO = marketService.getMarketByCategoryIdAndName(categoryId, name);
        System.out.println(marketDTO.getName());
        return ResponseEntity.ok(marketDTO);
    }
}
