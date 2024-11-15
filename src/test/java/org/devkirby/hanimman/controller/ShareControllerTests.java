package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.controller.ShareController;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ShareControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ShareService shareService;

    @InjectMocks
    private ShareController shareController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shareController).build();
    }

    @Test
    public void testCreateShare() throws Exception {
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setTitle("Test Title");
        shareDTO.setContent("Test Content");

        User user = new User();
        user.setId(1);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "나눠요 게시글 작성에 성공했습니다.");

        doNothing().when(shareService).create(any(ShareDTO.class));

        mockMvc.perform(post("/api/v1/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\", \"content\":\"Test Content\"}")
                        .principal(() -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("나눠요 게시글 작성에 성공했습니다."));
    }

    @Test
    public void testReadShare() throws Exception {
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setId(1);
        shareDTO.setTitle("Test Title");
        shareDTO.setContent("Test Content");

        User user = new User();
        user.setId(1);

        when(shareService.read(anyInt(), any(User.class))).thenReturn(shareDTO);

        mockMvc.perform(get("/api/v1/share/1")
                        .principal(() -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));
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