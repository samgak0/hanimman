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
        User user = userRepository.findById(2).orElseThrow();
        Address address = addressRepository.findById("1111010200").orElseThrow();

        List<Together> togethers = IntStream.range(0, 30)
                .mapToObj(i -> Together.builder()
                        .title("Together Title " + i)
                        .content("Together Content " + i)
                        .meetingAt(Instant.now().plus(i* 2L, ChronoUnit.HOURS))
                        .user(user)
                        .address(address)
                        .build())
                .collect(Collectors.toList());

        togetherRepository.saveAll(togethers);
    }

}
