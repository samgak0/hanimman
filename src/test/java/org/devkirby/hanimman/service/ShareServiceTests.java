package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareServiceTests {
    @Autowired
    private ShareService shareService;

    private static final Logger log = LoggerFactory.getLogger(ShareServiceTests.class);

    @Test
    @DisplayName("나눠요 게시글 수정 테스트")
    public void test1() {
        ShareDTO shareDTO = shareService.read(1);
        shareDTO.setTitle("수정된 제목 입니다");
        shareService.update(shareDTO);
    }

    @Test
    @DisplayName("나눠요 게시글 삭제 테스트")
    public void test2() {
        shareService.delete(1);
    }
}