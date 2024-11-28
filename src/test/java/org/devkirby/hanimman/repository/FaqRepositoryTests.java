package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@SpringBootTest
public class FaqRepositoryTests {

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertFaqs() {
        User user = userRepository.findById(1).orElseThrow();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Faq faq = Faq.builder()
                    .title("Sample FAQ Title " + i)
                    .content("Sample FAQ Content " + i)
                    .views(0)
                    .createdAt(Instant.now().minus(i, i % 2 == 0 ? ChronoUnit.DAYS : ChronoUnit.HOURS))
                    .user(user)
                    .build();
            faqRepository.save(faq);
        });
    }
}