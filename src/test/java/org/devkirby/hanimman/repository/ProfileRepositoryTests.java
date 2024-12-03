package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

@SpringBootTest
public class ProfileRepositoryTests {

    @Autowired
    private ProfileRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void insertProfileTest() {
        // User 객체 생성
       Optional<User> opt = userRepository.findById(2);
        User user = opt.orElseThrow(() -> new RuntimeException("User not found"));

        // Profile 객체 생성
        Profile profile = Profile.builder()
                .realName("aaa")
                .serverName("bbbb")
                .mineType("jpg")
                .fileSize(1000)
                .createdAt(Instant.now())
                .parent(user)  // User 객체를 parent로 설정
                .build();

        // profile 객체 저장
        repository.save(profile);

    }
    @Test
    void findbyUser() {
        // User 객체 생성
        Optional<User> opt = userRepository.findById(1);
        User user = opt.orElseThrow(() -> new RuntimeException("User not found"));

        // profile 객체 저장
        repository.findByParent(user);

    }

    @Test
    void updateProfileTest() {
        // User 객체 생성
        Optional<User> opt = userRepository.findById(1);
        User user = opt.orElseThrow(() -> new RuntimeException("User not found"));

        // Profile 객체 생성
        Profile profile = Profile.builder()
                .id(1)
                .realName("abc")
                .serverName("11222333aa")
                .mineType("jpg")
                .fileSize(1000)
                .createdAt(Instant.now())
                .parent(user)  // User 객체를 parent로 설정
                .build();

        // profile 객체 저장
        repository.save(profile);

    }


}
