package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProfileRepositoryTests {

    @Autowired
    private ProfileRepository profileRepository;

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
        profileRepository.save(profile);

    }
    @Test
    @Transactional
    void findByUser() {
        // User 객체 조회
        Optional<User> opt = userRepository.findById(1); // Long 타입 맞춤
        User user = opt.orElseThrow(() -> new RuntimeException("User not found"));

        // Profile 조회
        Profile profile = profileRepository.findByParent(user);

        // 결과 검증
        assertNotNull(profile, "Profile should not be null");
        assertEquals(user.getId(), profile.getParent().getId(), "Profile's parent ID should match User ID");
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
        profileRepository.save(profile);

    }

    @Test
    void selectByUser(){
        Optional<User> opt = userRepository.findById(2);
        User user = opt.orElseThrow(() -> new RuntimeException("User not found"));
        profileRepository.findByParent(user);
    }


}
