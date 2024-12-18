package org.devkirby.hanimman;

import org.devkirby.hanimman.entity.*;
import org.devkirby.hanimman.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ScenarioTests {
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private TogetherRepository togetherRepository;
    @Autowired
    private TogetherImageRepository togetherImageRepository;
    @Autowired
    private ShareImageRepository shareImageRepository;
    @Autowired
    private ShareParticipantRepository shareParticipantRepository;
    @Autowired
    private TogetherParticipantRepository togetherParticipantRepository;
    @Autowired
    private TogetherLocationRepository togetherLocationRepository;
    @Autowired
    private ShareLocationRepository shareLocationRepository;
    @Autowired
    private TogetherReviewRepository togetherReviewRepository;
    @Autowired
    private ShareReviewRepository shareReviewRepository;

    @Test
    @DisplayName("시연용 통합 데이터 User")
    public void scenario1(){
        List<User> users = new ArrayList<>();
        Random random = new Random();

        String[] maleNames = {"김철수", "이민수", "박영수", "최강수", "장동건", "정우성", "조인성", "유재석", "이광수", "송중기", "이병헌", "강호동", "이승기", "차은우", "박보검"};
        String[] femaleNames = {"김민지", "이수민", "박지수", "최은지", "장서연", "정다은", "조민서", "유진아", "이지은", "송지효", "이효리", "강지연", "이유정", "차예린", "박보영"};

        for (int i = 0; i < 30; i++) {
            String name;
            Gender gender;
            if (i % 2 == 0) {
                gender = Gender.M;
                name = maleNames[random.nextInt(maleNames.length)];
            } else {
                gender = Gender.F;
                name = femaleNames[random.nextInt(femaleNames.length)];
            }

            // 생일 생성 (1970 ~ 2005년 사이 랜덤)
            int year = 1970 + random.nextInt(36); // 1970~2005
            int month = 1 + random.nextInt(12); // 1~12
            int day = 1 + random.nextInt(28); // 1~28 (안전한 날짜 범위)
            LocalDate birth = LocalDate.of(year, month, day);

            // 랜덤 전화번호 생성
            String phonenum = "010-" + (1000 + random.nextInt(9000)) + "-" + (1000 + random.nextInt(9000));

            // 랜덤 닉네임 생성
            String[] nicknames = {"바람소리", "푸른하늘", "햇살", "노을빛", "별빛", "파도소리", "달빛", "새벽바람", "비오는날", "산들바람"};
            String nickname = nicknames[random.nextInt(nicknames.length)];

            // 랜덤 코드 번호 생성 (영소문자, 영대문자, 숫자 포함 6글자)
            String codenum = random.ints(6, 0, 62)
                    .mapToObj(n -> {
                        if (n < 10) return String.valueOf((char) ('0' + n)); // 숫자
                        if (n < 36) return String.valueOf((char) ('a' + n - 10)); // 소문자
                        return String.valueOf((char) ('A' + n - 36)); // 대문자
                    })
                    .reduce("", String::concat);

            // User 객체 생성 및 저장
            User user = User.builder()
                    .name(name)
                    .birth(birth)
                    .gender(gender)
                    .phonenum(phonenum)
                    .nickname(nickname)
                    .codenum(codenum)
                    .build();
            users.add(user);
        }

        // 저장
        users.forEach(user -> userRepository.save(user));
    }

    static class AddressData {
        private final String code;
        private final String latitude;
        private final String longitude;
        private final String detail;

        public AddressData(String code, String latitude, String longitude, String detail) {
            this.code = code;
            this.latitude = latitude;
            this.longitude = longitude;
            this.detail = detail;
        }

        public String getCode() {
            return code;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getDetail() {
            return detail;
        }
    }
    @Test
    @DisplayName("시연용 통합 데이터 Together")
    public void scenario2(){
        Random random = new Random();

        // 품목 리스트
        List<String> items = Arrays.asList("하인즈 케첩", "마요네즈", "맥주 세트", "양말 세트", "옷걸이 세트");

        // 주소 데이터
        List<AddressData> addresses = Arrays.asList(
                new AddressData("2650010100", "35.1785037434279", "129.115262179836", "코스트코 수영점"),
                new AddressData("1153010600", "37.498577", "126.859771", "코스트코 고척점"),
                new AddressData("2623010800", "35.163988", "129.052760", "트레이더스 서면점"),
                new AddressData("2647010200", "35.185019", "129.112006", "트레이더스 연산점"),
                new AddressData("1165010200", "37.4619732088672", "127.03615282356", "코스트코 양재점")
        );

        // 데이터 생성
        for (int i = 0; i < 200; i++) { // 100개의 게시글 생성
            // 품목 및 가격/수량 설정
            String item = items.get(random.nextInt(items.size()));
            int quantity = 2 + random.nextInt(4); // 최소 2개에서 최대 5개
            int price = 10000 * quantity; // 1개당 10,000원 기준 가격 설정

            // 사용자 랜덤 선택
            int userId = 1 + random.nextInt(30); // 1~30번 사용자

            // 주소 랜덤 선택
            AddressData addressData = addresses.get(random.nextInt(addresses.size()));

            // meetingAt 설정 (3시간 ~ 5일 이후 랜덤)
            long hoursToAdd = 3L + random.nextInt(120); // 3시간부터 5일(120시간)까지 랜덤
            Instant meetingAt = Instant.now().plus(hoursToAdd, ChronoUnit.HOURS);

            // Together 객체 생성
            Together together = Together.builder()
                    .title(item + " 나눠 가지실 분")
                    .content(item + " " + quantity + "개들이 " + price + "원, 각자 하나씩 나눠요.")
                    .price(price)
                    .quantity(quantity)
                    .item(item)
                    .meetingAt(meetingAt)
                    .user(userRepository.findById(userId).orElseThrow())
                    .address(addressRepository.findById(addressData.getCode()).orElseThrow())
                    .build();
            togetherRepository.save(together);

            // TogetherLocation 객체 생성
            TogetherLocation togetherLocation = TogetherLocation.builder()
                    .together(together)
                    .latitude(addressData.getLatitude())
                    .longitude(addressData.getLongitude())
                    .detail(addressData.getDetail())
                    .address(addressRepository.findById(addressData.getCode()).orElseThrow())
                    .build();
            togetherLocationRepository.save(togetherLocation);
        }
    }

    @Test
    @DisplayName("시연용 통합 데이터 Share")
    public void scenario3(){
        User user = userRepository.findById(1).orElseThrow();

    }
}
