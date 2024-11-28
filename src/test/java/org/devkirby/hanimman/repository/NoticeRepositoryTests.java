package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@SpringBootTest
public class NoticeRepositoryTests {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertNotices() {
        User user =  userRepository.findById(1).orElseThrow();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Notice notice = Notice.builder()
                    .title("Sample Title " + i)
                    .content("Sample Content " + i)
                    .views(0)
                    .createdAt(Instant.now().minus(i, i % 2 == 0 ? ChronoUnit.DAYS : ChronoUnit.HOURS))
                    .user(user)
                    .build();
            noticeRepository.save(notice);
        });
    }
}