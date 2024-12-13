package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Together;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TogetherRepository extends JpaRepository<Together, Integer> {

    // 특정 사용자 ID에 해당하는 Together 리스트 조회
    Page<Together> findByUserIdAndDeletedAtIsNull(Integer userId, Pageable pageable);

    // 제목에서 키워드 포함
    List<Together> findByTitleContainingAndDeletedAtIsNull(String keyword);

    // 제목과 내용에 하나의 키워드를 포함하여 검색
    @Query("SELECT t FROM Together t WHERE (t.title LIKE '%:keyword%' OR t.content LIKE '%:keyword%') AND t.deletedAt IS NULL")
    List<Together> findByTitleOrContentContainingAndDeletedAtIsNull(String keyword);

    // 품목에 키워드 포함
    List<Together> findByItemContainingAndDeletedAtIsNull(String keyword);

    Page<Together> findByTitleContainingOrContentContainingAndDeletedAtIsNull(String titleKeyword,
                                                                              String contentKeyword, Pageable pageable);

    Page<Together> findByTitleContainingOrContentContaining(String titleKeyword,
                                                          String contentKeyword, Pageable pageable);

    Page<Together> findByIsEndIsFalseAndDeletedAtIsNull(Pageable pageable);

    List<Together> findByIsEndIsFalse();

    Page<Together> findByDeletedAtIsNull(Pageable pageable);

    // 아래쪽은 지역 기반 검색
//    Page<Together> findByAddress_CityCodeAndAddress_DistrictCodeAndIsEndIsFalseAndDeletedAtIsNull
//            (Pageable pageable, String cityCode, String districtCode);
//
//    Page<Together> findByAddress_CityCodeAndAddress_DistrictCodeAndDeletedAtIsNull
//            (Pageable pageable, String cityCode, String districtCode);
//
//    Page<Together>  findByAddress_CityCodeAndAddress_DistrictCodeAndTitleContainingOrContentContainingAndDeletedAtIsNull
//            (Pageable pageable, String cityCode, String districtCode, String titleKeyword, String contentKeyword);

    Page<Together> findByAddress_CityCodeAndIsEndIsFalseAndDeletedAtIsNull
            (Pageable pageable, String cityCode);

    Page<Together> findByAddress_CityCodeAndDeletedAtIsNull
            (Pageable pageable, String cityCode);

    Page<Together> findByAddress_CityCodeAndTitleContainingOrContentContainingAndIsEndIsFalseAndDeletedAtIsNull
            (Pageable pageable, String cityCode, String titleKeyword, String contentKeyword);

    Page<Together> findByAddress_CityCodeAndTitleContainingOrContentContainingAndDeletedAtIsNull
            (Pageable pageable, String cityCode, String titleKeyword, String contentKeyword);
}
