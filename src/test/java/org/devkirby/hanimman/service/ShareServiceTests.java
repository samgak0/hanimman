package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ShareServiceTests {
    @Autowired
    private ShareService shareService;

    private static final Logger log = LoggerFactory.getLogger(ShareServiceTests.class);

//    @Test
//    @DisplayName("나눠요 게시글 수정 테스트")
//    public void test1() {
//        ShareDTO shareDTO = shareService.read(1, null);
//        shareDTO.setTitle("수정된 제목 입니다");
//        shareService.update(shareDTO);
//    }

//    @Test
//    @DisplayName("나눠요 게시글 삭제 테스트")
//    public void test2() {
//        shareService.delete(1);
//    }

    @Test
    @DisplayName("나눠요 게시글 전체 리스트 테스트")
    public void testListAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ShareDTO> shareDTOPage = shareService.listAll(pageable, false);
        assertFalse(shareDTOPage.isEmpty(), "게시글 리스트가 비어있지 않아야 합니다.");

        shareDTOPage.forEach(shareDTO -> log.info("ShareDTO: {}", shareDTO));
    }

    @Test
    @DisplayName("나눠요 게시글 read 테스트")
    public void testRead(){
        ShareDTO shareDTO = shareService.read(20, null);
        log.info("ShareDTO: {}", shareDTO);
    }
}