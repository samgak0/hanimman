CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID',
    name VARCHAR(20) NOT NULL COMMENT '이름',
    birth DATE NOT NULL COMMENT '생년월일',
    gender ENUM('M', 'W') NOT NULL COMMENT '성별',
    phonenum VARCHAR(20) NOT NULL COMMENT '전화번호',
    nickname VARCHAR(20) NOT NULL COMMENT '닉네임',
    codenum VARCHAR(6) NOT NULL COMMENT '고유코드번호 (6글자 영문자)',
    privilege BIT NOT NULL COMMENT '권한(관리자 권한)',
    blocked_at DATETIME(6) COMMENT '차단일 (NULL 이 아니면 차단)',
    primary_region_id INT COMMENT '기본 동 ID',
    secondly_region_id INT COMMENT '선택적 동 ID',
    device_unique_num VARCHAR(255) NOT NULL COMMENT '기기고유값',
    modified_at DATETIME(6) COMMENT '수정일',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '가입일',
    deleted_at DATETIME(6) COMMENT '탈퇴일',
    FOREIGN KEY (primary_region_id) REFERENCES region(id),
    FOREIGN KEY (secondly_region_id) REFERENCES region(id)
) COMMENT = '회원';

CREATE TABLE city (
    id INT PRIMARY KEY COMMENT '시/도 ID',
    name VARCHAR(50) NOT NULL COMMENT '시/도 명'
) COMMENT = '시/광역시/특별시/도';

CREATE TABLE country (
    id INT PRIMARY KEY COMMENT '구/군 ID',
    parent_id INT COMMENT '시/도 ID',
    name VARCHAR(50) NOT NULL COMMENT '구/군 명',
    FOREIGN KEY (parent_id) REFERENCES city(id)
) COMMENT = '구/군';

CREATE TABLE region (
    id INT PRIMARY KEY COMMENT '동 ID',
    parent_id INT COMMENT '구/군 ID',
    name VARCHAR(50) NOT NULL COMMENT '동 명',
    FOREIGN KEY (parent_id) REFERENCES country(id)
) COMMENT = '동';

CREATE TABLE authentication (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자본인인증로그 ID',
    value VARCHAR(255) NOT NULL COMMENT '본인인증값',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '사용자본인인증로그';

CREATE TABLE profile (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필사진 ID',
    real_name VARCHAR(255) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(255) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(255) NOT NULL COMMENT '사진타입',
    file_size INT NOT NULL COMMENT '크기',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    parent_id INT COMMENT '소속 게시글',
    FOREIGN KEY (parent_id) REFERENCES user(id)
) COMMENT = '프로필사진';

CREATE TABLE share (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT DEFAULT 0 COMMENT '조회수',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '작성일',
    modified_at DATETIME(6) COMMENT '수정일',
    deleted_at DATETIME(6) COMMENT '삭제일',
    region_id INT COMMENT '글 소속 동',
    location VARCHAR(255) COMMENT '모임장소',
    location_date DATETIME(6) COMMENT '모임일',
    item VARCHAR(255) COMMENT '품목',
    quantity INT COMMENT '수량',
    is_end TINYINT DEFAULT 0 COMMENT '마감여부',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (region_id) REFERENCES region(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '나눠요';

CREATE TABLE share_images (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필 사진 ID',
    original_name VARCHAR(255) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(255) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(255) NOT NULL COMMENT '사진타입',
    user_id INT COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent_id INT COMMENT '소속 게시글',
    deleted_at DATETIME(6) COMMENT '삭제일',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
) COMMENT = '나눠요 이미지';

CREATE TABLE share_participants (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '참여자 신청 번호',
    date DATETIME(6) NOT NULL COMMENT '요청일',
    quantity INT NOT NULL COMMENT '수량',
    rejected TINYINT DEFAULT 0 COMMENT '작성자 거절 여부',
    user_id INT COMMENT '참여자 ID',
    parent_id INT COMMENT '소속 게시글',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
) COMMENT = '나눠요 참여자 신청';

CREATE TABLE share_favorites (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '찜 번호',
    user_id INT COMMENT '사용자 ID',
    parent_id INT COMMENT '게시글 코드',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
) COMMENT = '나눠요 찜';

CREATE TABLE share_reports (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 ID',
    reporter_id INT COMMENT '신고자 ID',
    reported_id INT COMMENT '신고당한자 ID',
    category_id INT COMMENT '신고카테고리',
    parent_id INT COMMENT '나눠요 신고당한 게시글 ID',
    FOREIGN KEY (reporter_id) REFERENCES user(id),
    FOREIGN KEY (reported_id) REFERENCES user(id),
    FOREIGN KEY (category_id) REFERENCES report_category(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
) COMMENT = '나눠요 신고';

CREATE TABLE share_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰글 번호',
    content VARCHAR(255) NOT NULL COMMENT '리뷰 내용',
    create_at DATETIME(6) NOT NULL COMMENT '작성일',
    deleted_at DATETIME(6) COMMENT '수정일',
    member_id INT COMMENT '회원 번호',
    parent_id INT COMMENT '소속 게시글',
    FOREIGN KEY (member_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
) COMMENT = '나눠요 후기';

CREATE TABLE together (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT DEFAULT 0 COMMENT '조회수',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '작성일',
    modified_at DATETIME(6) COMMENT '수정일',
    deleted_at DATETIME(6) COMMENT '삭제일',
    region_id INT COMMENT '글 소속 동',
    meeting_location VARCHAR(255) COMMENT '모임장소',
    meeting_at DATETIME(6) COMMENT '모임일',
    item VARCHAR(255) COMMENT '품목',
    quantity INT DEFAULT 0 COMMENT '수량 (0일 경우 수량 미정)',
    is_end TINYINT DEFAULT 0 COMMENT '마감여부',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (region_id) REFERENCES region(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '같이가요';

CREATE TABLE together_images (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필사진 번호',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent_id INT COMMENT '소속 게시글',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    deleted_at DATETIME(6) COMMENT '삭제일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
) COMMENT = '같이가요 이미지';

CREATE TABLE together_participants (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '참여자 신청 번호',
    date DATETIME(6) NOT NULL COMMENT '요청일',
    quantity INT NOT NULL COMMENT '수량',
    rejected TINYINT DEFAULT 0 COMMENT '작성자 거절 여부',
    user_id INT COMMENT '참여자 ID',
    parent_id INT COMMENT '소속 게시글',
    deleted_at DATETIME(6) COMMENT '삭제일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
) COMMENT = '같이가요 참여자 신청';

CREATE TABLE together_favorites (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '찜 번호',
    user_id INT COMMENT '사용자 ID',
    parent_id INT COMMENT '부모 ID',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
) COMMENT = '같이가요 찜';

CREATE TABLE together_reports (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 ID',
    reporter_id INT COMMENT '신고자 ID',
    reported_id INT COMMENT '신고당한자 ID',
    category_id INT COMMENT '신고카테고리',
    parent_id INT COMMENT '같이가요 신고당한 게시글 ID',
    FOREIGN KEY (reporter_id) REFERENCES user(id),
    FOREIGN KEY (reported_id) REFERENCES user(id),
    FOREIGN KEY (category_id) REFERENCES report_category(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
) COMMENT = '같이가요 신고';

CREATE TABLE together_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰글 번호',
    content VARCHAR(255) NOT NULL COMMENT '리뷰 내용',
    create_at DATETIME(6) NOT NULL COMMENT '작성일',
    deleted_at DATETIME(6) COMMENT '수정일',
    member_id INT COMMENT '회원 번호',
    parent_id INT COMMENT '소속 게시글',
    FOREIGN KEY (member_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
) COMMENT = '같이가요 후기';


CREATE TABLE report_category (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '신고글 카테고리 번호',
    name VARCHAR(255) NOT NULL COMMENT '신고카테고리명',
    manager_id INT COMMENT '관리담당자',
    create_date DATETIME(6) COMMENT '생성일',
    deleted_at DATETIME(6) COMMENT '삭제여부',
    FOREIGN KEY (manager_id) REFERENCES user(id)
) COMMENT = '신고글 카테고리';

CREATE TABLE notices (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT DEFAULT 0 COMMENT '조회수',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '작성일',
    modified_at DATETIME(6) DEFAULT NOW(6) COMMENT '수정일',
    deleted_at DATETIME(6) COMMENT '삭제일',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '공지사항';

CREATE TABLE notice_files (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필사진 번호',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name TEXT NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent_id INT COMMENT '부모 ID',
    create_at DATETIME(6) COMMENT '생성일',
    deleted_at DATETIME(6) DEFAULT NOW(6) COMMENT '삭제일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES notices(id)
) COMMENT = '공지사항 파일';

CREATE TABLE inquiries (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '작성일',
    modified_at DATETIME(6) DEFAULT NOW(6) COMMENT '수정일',
    deleted_at DATETIME(6) COMMENT '삭제일',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '1:1문의';

CREATE TABLE inquire_files (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필사진 번호',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent_id INT COMMENT '소속 게시글',
    deleted_at DATETIME(6) COMMENT '삭제일',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES inquiries(id)
) COMMENT = '공지사항 파일';

CREATE TABLE faq (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT DEFAULT 0 COMMENT '조회수',
    faq_deleted_at DATETIME(6) COMMENT '삭제여부',
    faq_create_date DATETIME(6) COMMENT '작성일',
    faq_modification DATETIME(6) COMMENT '수정일',
    user_id INT COMMENT '회원 ID',
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = 'FAQ 게시판';

CREATE TABLE faq_files (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '프로필사진 ID',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent_id INT COMMENT '부모 ID',
    create_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES faq(id)
) COMMENT = 'FAQ 파일';

CREATE TABLE chat_messages (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '메시지 ID',
    user_id INT COMMENT '사용자 ID',
    room_id INT COMMENT '소속 ROOM ID (채팅방)',
    content VARCHAR(255) NOT NULL COMMENT '내용',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일',
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
) COMMENT = '채팅 메시지';

CREATE TABLE chat_rooms (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '방 ID',
    name VARCHAR(255) NOT NULL COMMENT '방 이름',
    created_at DATETIME(6) DEFAULT NOW(6) COMMENT '생성일'
) COMMENT = '참여자 채팅방 정보';

CREATE TABLE chat_participants (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '메시지 ID',
    room_id INT COMMENT '방 ID',
    user_id INT COMMENT '사용자 ID',
    joined_at DATETIME(6) DEFAULT NOW(6) COMMENT '참여자 입장일',
    FOREIGN KEY (room_id) REFERENCES chat_rooms(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT = '참여자 채팅 정보';

CREATE TABLE markets (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '마트ID',
    category INT COMMENT '마트 종류',
    name VARCHAR(50) NOT NULL COMMENT '마트 명',
    latitude VARCHAR(50) NOT NULL COMMENT '위도',
    longitude VARCHAR(50) NOT NULL COMMENT '경도',
    city_id INT COMMENT '시/도',
    contry_id INT COMMENT '구/군',
    region_id INT COMMENT '동',
    address VARCHAR(255) NOT NULL COMMENT '주소',
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (contry_id) REFERENCES country(id),
    FOREIGN KEY (region_id) REFERENCES region(id)
) COMMENT = '마트';

CREATE TABLE market_categories (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 ID',
    type INT NOT NULL COMMENT '마트 타입 (ex: 1 = 코스트코, 2 = 트레이더스)',
    name VARCHAR(50) NOT NULL COMMENT '마트 종류 이름'
) COMMENT = '마트 카테고리';
