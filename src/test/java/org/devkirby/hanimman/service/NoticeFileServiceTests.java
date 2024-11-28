package org.devkirby.hanimman.service;

import org.devkirby.hanimman.util.ImageUploadUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class NoticeFileServiceTests {
    @Autowired
    private NoticeFileService noticeFileService;

    @Autowired
    private ImageUploadUtil imageUploadUtil;

    @Test
    public void testUploadImage() throws IOException {
        // Arrange
        Path imagePath = Paths.get("heinz2.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MultipartFile mockFile = new MockMultipartFile(
                "file", "heinz2.png",
                "image/jpeg", imageBytes);

        // Act
        String result = noticeFileService.uploadFile(mockFile, 2);

        // Assert
        assertTrue(result.contains("heinz2.png"));
    }
}
