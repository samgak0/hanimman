package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Together;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
class TogetherRepositoryTests {

    private static final Logger log = LoggerFactory.getLogger(TogetherRepositoryTests.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private TogetherRepository togetherRepository; // 추가된 부분

    @Test
    @DisplayName("같이가요 게시글 등록 테스트")
    public void test1() {
        Together together = Together.builder()
                .title("락스 같이사요")
                .content("락스 3L짜리 3통 파는거 같이 구매하러가실분?")
                .views(3)
                .createdAt(Instant.now())
                .quantity(3)
                .isEnd(false)
                .user(userRepository.findById(2).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")))
                .address(addressRepository.findById("1111010200")
                        .orElseThrow(() -> new RuntimeException("주소를 찾을 수 없습니다.")))
                .build();

        log.info("Saving together: {}", together);
        togetherRepository.save(together);
    }
}
