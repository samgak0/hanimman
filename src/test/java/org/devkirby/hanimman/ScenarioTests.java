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
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private FaqRepository faqRepository;

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
        Random random = new Random();

        // 품목 리스트
        List<String> items = Arrays.asList(
                "락스 3L *3", "과자 세트", "옷걸이 10개", "맥주 500ml *6", "음료수 1.5L *4", "방향제 5개",
                "세제 2L *3", "라면 20개입", "휴지 30롤", "샴푸 1L *2");

        // 제목과 내용 패턴
        List<String> titles = Arrays.asList(
                "함께 나눠요", "묶음으로 샀어요", "필요하신 분?", "저렴하게 나눠요", "같이 나눠 사요");
        List<String> details = Arrays.asList(
                "마트 앞에서 나눠요", "아파트 정문에서 만나기", "버스 정류장 근처", "주차장에서 나눌게요",
                "대형마트 입구에서 만나기");

        // 서울과 부산 법정동 코드 및 좌표
        List<AddressData> addresses = Arrays.asList(
                new AddressData("1111010200", "37.570000", "126.985000", "서울 종로구 청운동"),
                new AddressData("1168010100", "37.498000", "127.027000", "서울 강남구 역삼동"),
                new AddressData("2635010400", "35.1862088", "129.1231358", "부산 해운대구 우동"),
                new AddressData("2644010200", "35.179600", "129.075000", "부산 연제구 연산동"),
                new AddressData("2611011700", "35.098200", "129.024500", "부산 중구 중앙동"));

        for (int i = 0; i < 200; i++) {
            // 랜덤 유저
            User user = userRepository.findById(1 + random.nextInt(30)).orElseThrow();

            // 랜덤 품목과 수량
            String item = items.get(random.nextInt(items.size()));
            int quantity = 2 + random.nextInt(4); // 2~5개
            int price = 10000 + (random.nextInt(10) * 1000); // 10,000 ~ 19,000원

            // 랜덤 주소
            AddressData addressData = addresses.get(random.nextInt(addresses.size()));

            // 랜덤 날짜
            Instant createdAt = Instant.now();
            Instant locationDate = createdAt.plus(1 + random.nextInt(7), ChronoUnit.DAYS);

            // 랜덤 제목과 내용
            String title = titles.get(random.nextInt(titles.size())) + " - " + item;
            String content = item + " " + quantity + "개 묶음으로 샀어요. 필요하신 분 나눠요.";

            // Share 객체 생성
            Share share = Share.builder()
                    .title(title)
                    .content(content)
                    .quantity(quantity)
                    .price(price)
                    .item(item)
                    .createdAt(createdAt)
                    .locationDate(locationDate)
                    .user(user)
                    .address(addressRepository.findById(addressData.getCode()).orElseThrow())
                    .build();
            shareRepository.save(share);

            // ShareLocation 객체 생성
            ShareLocation shareLocation = ShareLocation.builder()
                    .share(share)
                    .latitude(addressData.getLatitude())
                    .longitude(addressData.getLongitude())
                    .detail(details.get(random.nextInt(details.size())))
                    .address(addressRepository.findById(addressData.getCode()).orElseThrow())
                    .build();
            shareLocationRepository.save(shareLocation);
        }
    }

    @Test
    @DisplayName("시연용 통합 데이터 notice")
    public void scenario4(){
        User user = userRepository.findById(1).orElseThrow();

        List<Notice> notices = Arrays.asList(
                Notice.builder()
                        .title("12월 21일 점검 일정입니다.")
                        .content("12월 21일 04:00 ~ 06:00까지 서버 점검이 진행됩니다. 이 시간 동안 서비스 이용이 불가합니다.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("신규 카테고리 추가 안내")
                        .content("의류 및 생활용품 카테고리가 추가되었습니다! 더 많은 제품을 함께 나눠보세요.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("안전 거래를 위한 유의사항")
                        .content("직거래 시 공공장소에서 만나세요. 안전하고 즐거운 거래를 위해 꼭 지켜주세요.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("12월 이벤트 안내")
                        .content("12월 한 달간 첫 거래를 완료한 유저에게 1,000원 할인 쿠폰을 드립니다! 많은 참여 부탁드립니다.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("서비스 약관 개정 안내")
                        .content("2024년 1월 1일부터 새로운 서비스 이용 약관이 적용됩니다. 확인 부탁드립니다.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("앱 업데이트 공지")
                        .content("새로운 버전의 앱이 출시되었습니다. 업데이트 후 더욱 편리한 서비스를 이용해보세요.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("불법 거래 금지 안내")
                        .content("주류 및 유해 물품의 거래는 법적으로 금지되어 있습니다. 거래 등록 시 유의해 주세요.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("시스템 점검 안내")
                        .content("12월 28일 02:00 ~ 05:00까지 서버 점검이 예정되어 있습니다. 서비스 이용에 참고해 주세요.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("겨울철 배송 유의사항")
                        .content("겨울철 날씨로 인해 일부 배송이 지연될 수 있습니다. 양해 부탁드립니다.")
                        .user(user)
                        .build(),
                Notice.builder()
                        .title("리뷰 기능 개선 안내")
                        .content("사용자 요청을 반영해 리뷰 작성 시 사진 업로드 기능이 추가되었습니다!")
                        .user(user)
                        .build()
        );

        // 공지사항 저장
        notices.forEach(noticeRepository::save);
    }

    @Test
    @DisplayName("시연용 통합 데이터 FAQ")
    public void scenario5(){
        User user = userRepository.findById(1).orElseThrow();

        List<Faq> faqs = Arrays.asList(
                Faq.builder()
                        .title("거래 등록은 어떻게 하면 되나요?")
                        .content("거래 등록은 메인 화면 우측 상단의 '거래 등록' 버튼을 눌러 진행할 수 있습니다. " +
                                "거래 등록 시 제목, 내용, 수량, 가격, 품목, 사진 등을 입력해주세요. 거래 등록 후 다른 사용자가 신청하면 거래가 성사됩니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("직거래 장소는 어디에서 만나는 게 좋나요?")
                        .content("직거래는 안전을 위해 사람이 많은 공공장소나 CCTV가 있는 곳에서 진행하는 것이 좋습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("가격은 어떻게 책정하면 되나요?")
                        .content("실제 구매 가격을 기준으로 책정해주세요. 실제 영수증 가격과 다를 경우 제재를 받을 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("거래 취소는 어떻게 하나요?")
                        .content("거래 취소는 마이페이지의 '나의 거래' 메뉴에서 취소하고 싶은 거래를 선택 후 '거래 취소' 버튼을 누르면 됩니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("거래 신청 후 상대방이 응답하지 않아요.")
                        .content("거래 신청 후 24시간 이내에 상대방이 응답하지 않으면 자동으로 거래가 취소됩니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("거래가 성사된 후 결제는 어떻게 하나요?")
                        .content("현재 서비스는 직거래 기반이므로 현장에서 직접 결제하셔야 합니다. 안전 거래를 위해 현금보다는 계좌이체를 권장합니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("품목에 대한 사진은 꼭 등록해야 하나요?")
                        .content("사진 등록은 필수는 아니지만 거래의 신뢰도를 높이기 위해 사진을 등록하는 것을 권장합니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("비밀번호를 잊어버렸어요. 어떻게 해야 하나요?")
                        .content("로그인 화면의 '비밀번호 찾기'를 클릭한 후 본인 인증 절차를 거치면 비밀번호를 재설정할 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("부적절한 거래를 발견했어요. 신고는 어떻게 하나요?")
                        .content("부적절한 거래는 거래 상세 페이지 하단의 '신고하기' 버튼을 눌러 신고할 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("아이디를 변경할 수 있나요?")
                        .content("아이디는 최초 설정 후 변경이 불가능합니다. 닉네임은 설정 메뉴에서 수정이 가능합니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("거래 완료 후 상대방의 평가를 작성해야 하나요?")
                        .content("평가 작성은 선택 사항입니다. 하지만 평가를 남기면 상대방의 신뢰도를 확인할 수 있어 더 나은 거래 환경을 만들 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("주소를 변경하려면 어떻게 해야 하나요?")
                        .content("마이페이지의 '내 정보 관리'에서 주소를 변경할 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("대형마트에서 구매한 품목만 거래 가능한가요?")
                        .content("대형마트에서 구매한 품목이 아니더라도 공산품이나 나눠 사용하기 적합한 제품이라면 등록할 수 있습니다.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("거래 등록 시 입력한 정보는 수정할 수 있나요?")
                        .content("거래 등록 후에는 거래 신청이 없을 경우에만 수정이 가능합니다. '나의 거래' 메뉴에서 수정해주세요.")
                        .user(user)
                        .build(),
                Faq.builder()
                        .title("계정 탈퇴는 어떻게 하나요?")
                        .content("계정 탈퇴는 설정 메뉴의 '계정 관리'에서 탈퇴를 진행할 수 있습니다. 탈퇴 시 모든 거래 기록이 삭제되니 신중하게 결정해주세요.")
                        .user(user)
                        .build()
        );

        // FAQ 저장
        faqs.forEach(faqRepository::save);
    }
}
