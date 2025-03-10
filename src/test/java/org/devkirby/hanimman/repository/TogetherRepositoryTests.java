package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class TogetherRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(TogetherRepositoryTests.class);
    @Autowired
    private TogetherRepository togetherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("임의의 Together 데이터 30개 생성 테스트")
    public void createRandomTogetherData() {
        User user = userRepository.findById(4).orElseThrow();
        Address address = addressRepository.findById("1111010200").orElseThrow();

        List<Together> togethers = IntStream.range(0, 20)
                .mapToObj(i -> Together.builder()
                        .title("같이가요 제목 " + i * 3)
                        .content("같이가요 내용 " + i * 3)
                        .meetingAt(Instant.now().plus(i* 2L, ChronoUnit.HOURS))
                        .user(user)
                        .address(address)
                        .quantity(4)
                        .build())
                .collect(Collectors.toList());

        togetherRepository.saveAll(togethers);
    }

    @Test
    @DisplayName("together address 조회")
    public void readTogetherAddress() {
        Together together = togetherRepository.findById(1).orElseThrow();
        String address = together.getAddress().getCityName();
        log.info("together address : {}", together);
    }
}
