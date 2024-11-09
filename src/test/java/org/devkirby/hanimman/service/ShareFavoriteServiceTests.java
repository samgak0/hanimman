package org.devkirby.hanimman.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShareFavoriteServiceTests {
    @Autowired
    private ShareFavoriteService shareFavoriteService;


    private static final Logger log = LoggerFactory.getLogger(ShareFavoriteServiceTests.class);

    @Test
    @DisplayName("나눠요 게시글 좋아요 테스트")
    public void test1() {
        shareFavoriteService.create(3, 2);
        log.info("좋아요 수 :" + shareFavoriteService.countByParentId(2));
    }
}