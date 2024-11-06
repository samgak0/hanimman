CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    name VARCHAR(20) NOT NULL COMMENT '이름',
    birth DATE NOT NULL COMMENT '생년월일',
    gender ENUM('M', 'W') NOT NULL COMMENT '성별',
    phonenum VARCHAR(20) NOT NULL COMMENT '전화번호',
    nickname VARCHAR(20) NOT NULL COMMENT '닉네임',
    codenum VARCHAR(20) NOT NULL COMMENT '고유코드번호 - 6글자 영문자',
    privilege TINYINT NOT NULL COMMENT '권한(관리자 권한)',
    blacklist TINYINT NOT NULL COMMENT '블랙여부',
    neighborhood VARCHAR(255) NOT NULL COMMENT '동네',
    device_unique_num VARCHAR(255) NOT NULL COMMENT '기기고유값',
    modified_at TIMESTAMP NULL COMMENT '수정일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일',
    PRIMARY KEY (id)
);


CREATE TABLE notices (
    id INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수정일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일',
    member_id INT NOT NULL COMMENT '회원 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id)
);

CREATE TABLE city (
    id INT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '시/도 명',
    PRIMARY KEY (id)
);

CREATE TABLE contry (
    city_id INT NOT NULL COMMENT '시/도 ID',
    id INT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '시/도 명',
    PRIMARY KEY (id),
    FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE region (
    city_id INT NOT NULL COMMENT '시/도 ID',
    contry_id INT NOT NULL COMMENT '구/군 ID',
    id INT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '시/도 명',
    PRIMARY KEY (id),
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (contry_id) REFERENCES contry(id)
);

CREATE TABLE authentication (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    value VARCHAR(255) NOT NULL COMMENT '본인인증값',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    parent INT NOT NULL COMMENT '소속 게시글',
    PRIMARY KEY (id),
    FOREIGN KEY (parent) REFERENCES user(id)
);

CREATE TABLE profile (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    real_name VARCHAR(255) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(255) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(255) NOT NULL COMMENT '사진타입',
    file_size INT NOT NULL COMMENT '크기',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    parent INT NOT NULL COMMENT '소속 게시글',
    PRIMARY KEY (id),
    FOREIGN KEY (parent) REFERENCES user(id)
);

CREATE TABLE share (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    modified_at TIMESTAMP NULL COMMENT '수정일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일' COMMENT '탈퇴일',
    location VARCHAR(255) NOT NULL COMMENT '모임장소',
    location_date TIMESTAMP NOT NULL COMMENT '모임일',
    item VARCHAR(255) NOT NULL COMMENT '품목',
    quantity INT NOT NULL COMMENT '수량',
    is_end TINYINT NOT NULL DEFAULT 0 COMMENT '마감여부',
    member_id INT NOT NULL COMMENT '회원 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id)
);

CREATE TABLE share_images (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    original_name VARCHAR(255) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(255) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(255) NOT NULL COMMENT '사진타입',
    user_id INT NOT NULL COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent INT NOT NULL COMMENT '소속 게시글',
    deleted_at TIMESTAMP NULL COMMENT '삭제일' COMMENT '탈퇴일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES share(id)
);

CREATE TABLE share_participants (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    date TIMESTAMP NOT NULL COMMENT '요청일',
    quantity INT NOT NULL COMMENT '수량',
    rejected TINYINT NOT NULL DEFAULT 0 COMMENT '작성자 거절 여부',
    user_id INT NOT NULL COMMENT '참여자 ID',
    parent INT NOT NULL COMMENT '소속 게시글',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES share(id)
);

CREATE TABLE share_favorites (
    id INT NOT NULL AUTO_INCREMENT COMMENT '찜 번호',
    user_id INT NOT NULL COMMENT '사용자 ID',
    parent INT NOT NULL COMMENT '부모 ID',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES share(id)
);

CREATE TABLE together (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    modified_at TIMESTAMP NULL COMMENT '수정일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일' COMMENT '탈퇴일',
    meeting_location VARCHAR(255) NOT NULL COMMENT '모임장소',
    meeting_at TIMESTAMP NOT NULL COMMENT '모임일',
    item VARCHAR(255) NOT NULL COMMENT '품목',
    quantity INT NOT NULL COMMENT '수량 (0일 경우에 수량 미정)',
    is_end TINYINT NOT NULL DEFAULT 0 COMMENT '마감여부',
    member_id INT NOT NULL COMMENT '회원 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id)
);

CREATE TABLE together_images (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT NOT NULL COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent INT NOT NULL COMMENT '소속 게시글',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일' COMMENT '탈퇴일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES together(id)
);

CREATE TABLE together_participants (
    id INT NOT NULL AUTO_INCREMENT COMMENT '고유 ID',
    date TIMESTAMP NOT NULL COMMENT '요청일',
    quantity INT NOT NULL COMMENT '수량',
    rejected TINYINT NOT NULL DEFAULT 0 COMMENT '작성자 거절 여부',
    user_id INT NOT NULL COMMENT '참여자 ID',
    parent INT NOT NULL COMMENT '소속 게시글',
    deleted_at TIMESTAMP NULL COMMENT '삭제일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES share(id)
);

CREATE TABLE together_favorites (
    id INT NOT NULL AUTO_INCREMENT COMMENT '찜 번호',
    user_id INT NOT NULL COMMENT '사용자 ID',
    parent INT NOT NULL COMMENT '부모 ID',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES together(id)
);



CREATE TABLE inquiries (
    id INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수정일',
    deleted_at TIMESTAMP NULL COMMENT '삭제일',
    member_id INT NOT NULL COMMENT '회원 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id)
);

CREATE TABLE inquire_files (
    id INT NOT NULL AUTO_INCREMENT COMMENT '프로필사진 번호',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT NOT NULL COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent INT NOT NULL COMMENT '소속 게시글',
    deleted_at TIMESTAMP NULL COMMENT '삭제일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES inquiries(id)
);

CREATE TABLE faq (
    id INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    views INT NOT NULL DEFAULT 0 COMMENT '조회수',
    faq_deleted_at TIMESTAMP NULL COMMENT '삭제여부',
    faq_create_date TIMESTAMP NOT NULL COMMENT '작성일',
    faq_modification TIMESTAMP NOT NULL COMMENT '수정일',
    member_id INT NOT NULL COMMENT '회원 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id)
);

CREATE TABLE faq_files (
    id INT NOT NULL AUTO_INCREMENT COMMENT '프로필사진 ID',
    original_name VARCHAR(100) NOT NULL COMMENT '원본 사진 이름',
    server_name VARCHAR(100) NOT NULL COMMENT '서버 사진 이름',
    mine_type VARCHAR(20) NOT NULL COMMENT '사진타입',
    user_id INT NOT NULL COMMENT '회원 ID',
    file_size INT NOT NULL COMMENT '크기',
    parent INT NOT NULL COMMENT '부모 ID',
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent) REFERENCES faq(id)
);

CREATE TABLE reports (
    id INT NOT NULL AUTO_INCREMENT COMMENT '신고 ID',
    reporter_id INT NOT NULL COMMENT '신고자 ID',
    reported_id INT NOT NULL COMMENT '신고당한자 ID',
    category INT NOT NULL COMMENT '신고카테고리',
    reported_post_id INT NOT NULL COMMENT '신고당한 게시글 ID',
    PRIMARY KEY (id),
    FOREIGN KEY (reporter_id) REFERENCES user(id),
    FOREIGN KEY (reported_id) REFERENCES user(id),
    FOREIGN KEY (category) REFERENCES report_category(id),
    FOREIGN KEY (reported_post_id) REFERENCES share(id)
);

CREATE TABLE report_category (
    id INT NOT NULL AUTO_INCREMENT COMMENT '신고글 카테고리 번호',
    name VARCHAR(255) NOT NULL COMMENT '신고카테고리명',
    manager INT NOT NULL COMMENT '관리담당자',
    create_date TIMESTAMP NOT NULL COMMENT '생성일',
    deleted_at TIMESTAMP NULL COMMENT '삭제여부',
    PRIMARY KEY (id),
    FOREIGN KEY (manager) REFERENCES user(id)
);

CREATE TABLE reviews_together (
    id INT NOT NULL AUTO_INCREMENT COMMENT '리뷰글 번호',
    content VARCHAR(255) NOT NULL COMMENT '리뷰 내용',
    create_at TIMESTAMP NOT NULL COMMENT '작성일',
    deleted_at TIMESTAMP NULL COMMENT '수정일',
    member_id INT NOT NULL COMMENT '회원 번호',
    parent_id INT NOT NULL COMMENT '소속 게시글',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES together(id)
);

CREATE TABLE share_together (
    id INT NOT NULL AUTO_INCREMENT COMMENT '리뷰글 번호',
    content VARCHAR(255) NOT NULL COMMENT '리뷰 내용',
    create_at TIMESTAMP NOT NULL COMMENT '작성일',
    deleted_at TIMESTAMP NULL COMMENT '수정일',
    member_id INT NOT NULL COMMENT '회원 번호',
    parent_id INT NOT NULL COMMENT '소속 게시글',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES share(id)
);

CREATE TABLE chat_messages (
    id INT NOT NULL AUTO_INCREMENT COMMENT '메시지 ID',
    user_id INT NOT NULL COMMENT '사용자 ID',
    room_id INT NOT NULL COMMENT '소속 ROOM ID',
    content VARCHAR(255) NOT NULL COMMENT '내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
);

CREATE TABLE chat_rooms (
    id INT NOT NULL AUTO_INCREMENT COMMENT '방 ID',
    name VARCHAR(255) NOT NULL COMMENT '방 이름',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id)
);

CREATE TABLE chat_participants (
    id INT NOT NULL AUTO_INCREMENT COMMENT '메시지 ID',
    room_id INT NOT NULL COMMENT '방 ID',
    user_id INT NOT NULL COMMENT '사용자 ID',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES chat_rooms(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE markets (
    id INT NOT NULL AUTO_INCREMENT COMMENT '마트ID',
    category INT NOT NULL COMMENT '마트 종류',
    name VARCHAR(50) NOT NULL COMMENT '마트 명',
    latitude VARCHAR(50) NOT NULL COMMENT '위도',
    longitude VARCHAR(50) NOT NULL COMMENT '경도',
    city_id INT NOT NULL COMMENT '시/도',
    contry_id INT NOT NULL COMMENT '구/군',
    region_id INT NOT NULL COMMENT '동',
    address VARCHAR(255) NOT NULL COMMENT '주소',
    PRIMARY KEY (id),
    FOREIGN KEY (category) REFERENCES market_categories(id),
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (contry_id) REFERENCES contry(id),
    FOREIGN KEY (region_id) REFERENCES region(id)
);

CREATE TABLE market_categories (
    id INT NOT NULL AUTO_INCREMENT COMMENT '카테고리 ID',
    name VARCHAR(50) NOT NULL COMMENT '마트 종류 이름',
    type INT NOT NULL COMMENT '마트 타입',
    PRIMARY KEY (id)
);
