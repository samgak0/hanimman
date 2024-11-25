package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Gender;
import org.devkirby.hanimman.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTests.class);
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 정보 입력 테스트")
    public void test1() {
        User user = User.builder()
                .name("이용준")
                .birth(LocalDate.parse("1994-03-09"))
                .gender(Gender.M)
                .phonenum("010-3682-2901")
                .nickname("Dragon01")
                .codenum("dieRlq")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device02")
                .build();
        userRepository.save(user);
    }
    @Test
    @DisplayName("유저 정보 10개 입력 테스트")
    public void test2() {
        User user1 = User.builder()
                .name("김철수")
                .birth(LocalDate.of(1970, 1, 15))
                .gender(Gender.M)
                .phonenum("010-1234-5671")
                .nickname("철수")
                .codenum("aBcD12")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device01")
                .build();

        User user2 = User.builder()
                .name("박영희")
                .birth(LocalDate.of(1985, 5, 20))
                .gender(Gender.F)
                .phonenum("010-2345-6782")
                .nickname("영희")
                .codenum("XyZpQw")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device02")
                .build();

        User user3 = User.builder()
                .name("이준호")
                .birth(LocalDate.of(1992, 10, 3))
                .gender(Gender.M)
                .phonenum("010-3456-7893")
                .nickname("준호")
                .codenum("FgHiJk")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device03")
                .build();

        User user4 = User.builder()
                .name("최민지")
                .birth(LocalDate.of(2001, 4, 17))
                .gender(Gender.F)
                .phonenum("010-4567-8904")
                .nickname("민지")
                .codenum("LmNoPq")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device04")
                .build();

        User user5 = User.builder()
                .name("강다솜")
                .birth(LocalDate.of(1965, 11, 11))
                .gender(Gender.F)
                .phonenum("010-5678-9015")
                .nickname("다솜")
                .codenum("ZyXwVu")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device05")
                .build();

        User user6 = User.builder()
                .name("정호석")
                .birth(LocalDate.of(1999, 7, 22))
                .gender(Gender.M)
                .phonenum("010-6789-0126")
                .nickname("호석")
                .codenum("RstUvW")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device06")
                .build();

        User user7 = User.builder()
                .name("윤지혜")
                .birth(LocalDate.of(2003, 2, 9))
                .gender(Gender.F)
                .phonenum("010-7890-1237")
                .nickname("지혜")
                .codenum("MnoPQr")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device07")
                .build();

        User user8 = User.builder()
                .name("송민준")
                .birth(LocalDate.of(1982, 12, 25))
                .gender(Gender.M)
                .phonenum("010-8901-2348")
                .nickname("민준")
                .codenum("GhIjKl")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device08")
                .build();

        User user9 = User.builder()
                .name("황보라")
                .birth(LocalDate.of(1975, 6, 14))
                .gender(Gender.F)
                .phonenum("010-9012-3459")
                .nickname("보라")
                .codenum("WxYzAb")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device09")
                .build();

        User user10 = User.builder()
                .name("임도현")
                .birth(LocalDate.of(1995, 9, 30))
                .gender(Gender.M)
                .phonenum("010-0123-4560")
                .nickname("도현")
                .codenum("CdEfGh")
                .privilege(false)
                .primaryAddressId(null)
                .secondlyAddressId(null)
                .deviceUnique("device10")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);
        userRepository.save(user9);
        userRepository.save(user10);
    }
}