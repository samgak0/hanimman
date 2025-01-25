package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ShareRepository;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.service.ShareService;
import org.devkirby.hanimman.service.UserAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ShareControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ShareService shareService;

    @InjectMocks
    private ShareController shareController;

    @Autowired
    private ShareRepository shareRepository;

    private static final Logger log = LoggerFactory.getLogger(ShareControllerTests.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shareController).build();
    }

    // @Test
    // public void testCreateShare() throws Exception {
    // ShareDTO shareDTO = new ShareDTO();
    // shareDTO.setTitle("Test Title");
    // shareDTO.setContent("Test Content");
    //
    // User user = new User();
    // user.setId(1);
    // Optional<UserAddressDTO> userAddressDTO = userAddressRepository.findById(1);
    //
    // Map<String, Object> response = new HashMap<>();
    // response.put("code", 200);
    // response.put("msg", "나눠요 게시글 작성에 성공했습니다.");
    //
    // doNothing().when(shareService).create(any(ShareDTO.class),
    // userAddressDTO.get().getPrimaryAddressId());
    //
    // mockMvc.perform(post("/api/v1/share")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"title\":\"Test Title\", \"content\":\"Test Content\"}")
    // .principal(() -> "user"))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.code").value(200))
    // .andExpect(jsonPath("$.msg").value("나눠요 게시글 작성에 성공했습니다."));
    // }

    @Test
    public void testReadShare() throws Exception {
        // ShareDTO shareDTO = new ShareDTO();
        // shareDTO.setId(1);
        // shareDTO.setTitle("Test Title");
        // shareDTO.setContent("Test Content");
        //
        // User user = new User();
        // user.setId(1);
        //
        // when(shareService.read(anyInt(), any(User.class))).thenReturn(shareDTO);
        //
        // mockMvc.perform(get("/api/v1/share/1")
        // .principal(() -> "user"))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.id").value(1))
        // .andExpect(jsonPath("$.title").value("Test Title"))
        // .andExpect(jsonPath("$.content").value("Test Content"));
        shareController.readShare(21, null);
    }

    @Test
    @DisplayName("ID가 21번인 나눠요 게시글 조회 테스트")
    public void testFindById21() {
        Integer shareId = 21;
        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 21번인 게시글이 존재하지 않습니다."));

        log.info("Share ID: {}, Title: {}, Content: {}, CreatedAt: {}",
                share.getId(), share.getTitle(), share.getContent(), share.getCreatedAt());
        assertEquals(21, share.getId(), "ID가 21번이어야 합니다.");
    }

    @Test
    public void testUpdateShare() throws Exception {
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setId(1);
        shareDTO.setTitle("Updated Title");
        shareDTO.setContent("Updated Content");

        mockMvc.perform(put("/api/v1/share/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteShare() throws Exception {
        mockMvc.perform(delete("/api/v1/share/1"))
                .andExpect(status().isOk());
    }
}