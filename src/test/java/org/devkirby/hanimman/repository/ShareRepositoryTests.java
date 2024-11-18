package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.Share;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShareRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(ShareRepositoryTests.class);
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("나눠요 게시글 등록 테스트")
    public void test1() {
        Share share = Share.builder()
                .title("락스 나눠사요")
                .content("락스 3L짜리 3통 파는거 샀어요. 1통씩 사실분 연락부탁드려요")
                .quantity(3)
                .item("락스 3L")
                .user(userRepository.findById(2).orElseThrow())
                .address(addressRepository.findById("1111010200").get())
                .build();
        shareRepository.save(share);
    }

    @Test
    @DisplayName("나눠요 게시글 15개 등록 테스트")
    public void testMultipleShares() {
        List<Share> shares = List.of(
                Share.builder().title("케찹 나눠요").content("케찹 대용량 샀어요. 나누실 분 구해요.").quantity(2).item("케찹 500g").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("마요네즈 필요하신 분").content("마요네즈 1kg짜리 필요하신 분 나눠요.").quantity(3).item("마요네즈 1kg").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("세제 나눠요").content("대용량 세제 샀는데 너무 많아서 나누려구요.").quantity(4).item("세제 2L").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("소금 나눠요").content("소금 5kg 구매했는데 많이 남아서 나눠요.").quantity(1).item("소금 1kg").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("설탕 나누실 분").content("설탕 3kg 대량 구매했어요. 같이 나누실 분 찾습니다.").quantity(3).item("설탕 1kg").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("참기름 나눔").content("참기름 대용량인데 너무 많아요.").quantity(2).item("참기름 500ml").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("후추 나눠요").content("후추 1kg 나눠 드릴 분 구합니다.").quantity(3).item("후추 200g").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("간장 필요하신 분").content("간장 큰 병 샀는데 나눠 드려요.").quantity(2).item("간장 1L").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("식초 나누실 분").content("식초 큰 용량으로 샀어요. 나누실 분 찾습니다.").quantity(3).item("식초 1L").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("밀가루 나눠요").content("밀가루 2kg 구매했어요. 나누실 분 찾습니다.").quantity(4).item("밀가루 500g").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("물엿 필요하신 분").content("물엿 1.2kg 나누실 분 구합니다.").quantity(2).item("물엿 500g").user(userRepository.findById(2).orElseThrow()).build(),
                Share.builder().title("카레가루 나눠요").content("카레가루 대량 구매했어요. 필요하신 분 있나요?").quantity(3).item("카레가루 200g").user(userRepository.findById(2).orElseThrow()).build(),

                Share.builder().title("고추장 나눠요").content("고추장 1kg짜리 샀는데 다 못 쓸 것 같아요.").quantity(3).item("고추장 1kg").user(userRepository.findById(3).orElseThrow()).build(),
                Share.builder().title("된장 나눠요").content("된장 대용량이라 나눠드릴 분 구합니다.").quantity(2).item("된장 1kg").user(userRepository.findById(3).orElseThrow()).build(),

                Share.builder().title("소면 나눠요").content("소면 2kg 구매했어요. 필요하신 분 있나요?").quantity(2).item("소면 500g").user(userRepository.findById(4).orElseThrow()).build()
        );

        shares.forEach(shareRepository::save);
    }

    @Test
    @DisplayName("나눠요 게시글 Id로 조회 테스트")
    public void testFindById() {
        Integer userId = 2;
        List<Share> shares = shareRepository.findByUserIdAndDeletedAtIsNull(userId);

        log.info("=== User ID {}번 유저의 게시글 목록 ===", userId);
        shares.forEach(share -> log.info("Share ID: {}, Title: {}, Content: {}", share.getId(), share.getTitle(), share.getContent()));
    }

    @Test
    @DisplayName("제목 및 내용에 '용량' 키워드 포함된 게시글 조회")
    public void findByTitleOrContentContainingTest() {
        String keyword = "용량";
        List<Share> shares = shareRepository.findByTitleOrContentContainingAndDeletedAtIsNull(keyword, keyword);

        log.info("=== 제목 및 내용에 '{}' 키워드 포함된 게시글 목록 ===", keyword);
        shares.forEach(share -> log.info("Share ID: {}, Title: {}, Content: {}", share.getId(), share.getTitle(), share.getContent()));
    }

    @Test
    @DisplayName("품목에 '락스' 키워드 포함된 게시글 조회")
    public void findByItemContainingTest() {
        String keyword = "락스";
        List<Share> shares = shareRepository.findByItemContainingAndDeletedAtIsNull(keyword);

        log.info("=== 품목에 '{}' 키워드 포함된 게시글 목록 ===", keyword);
        shares.forEach(share -> log.info("Share ID: {}, Title: {}, Item: {}", share.getId(), share.getTitle(), share.getItem()));
    }
}