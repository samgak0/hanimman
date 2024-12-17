package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherParticipantDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TogetherParticipantServiceImplTests {
    @Autowired
    private TogetherParticipantService togetherParticipantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TogetherRepository togetherRepository;

    private static final Logger log = LoggerFactory.getLogger(ShareServiceTests.class);

    @Test
    @DisplayName("함께해요 참여자 생성 테스트")
    public void testCreate() {
        TogetherParticipantDTO togetherParticipantDTO = new TogetherParticipantDTO();
        togetherParticipantDTO.setParentId(17);
        togetherParticipantDTO.setUserId(3);
        togetherParticipantDTO.setDate(Instant.now());
        togetherParticipantDTO.setQuantity(1);
        togetherParticipantService.create(togetherParticipantDTO);
    }
}