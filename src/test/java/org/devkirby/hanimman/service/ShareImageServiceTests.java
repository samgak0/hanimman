package org.devkirby.hanimman.service;

import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.repository.ShareImageRepository;
import org.devkirby.hanimman.repository.ShareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShareImageServiceTests {

    @Autowired
    private ShareImageRepository shareImageRepository;

    @Autowired
    private ShareRepository shareRepository;

    private ShareImageServiceImpl shareImageService;

    private String uploadDir;

    @BeforeEach
    public void setUp() {
        uploadDir = "upload"; // 테스트용 업로드 디렉토리 설정
        shareImageService = new ShareImageServiceImpl(shareImageRepository, shareRepository, new ModelMapper());
        shareImageService.uploadDir = uploadDir; // 테스트용으로 uploadDir 수동 설정
    }

    @Test
    public void testUploadImage() throws IOException {
        // Arrange
        Path imagePath = Paths.get("heinzketchup.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MultipartFile mockFile = new MockMultipartFile(
                "file", "heinzketchup.jpg",
                "image/jpeg", imageBytes);

        // Act
        String result = shareImageService.uploadImage(mockFile, 1);

        // Assert
        assertTrue(result.contains("File uploaded successfully"));
        assertTrue(Files.exists(Paths.get(uploadDir, result.split(" ")[3])));
    }
}