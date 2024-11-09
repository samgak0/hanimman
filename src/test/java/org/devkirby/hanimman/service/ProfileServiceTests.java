package org.devkirby.hanimman.service;

import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProfileServiceTests {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Value("${com.devkirby.hanimman.upload}")
    private String uploadDir;

    @BeforeEach
    public void setUp() {
        profileService.setUploadDir("uploads/"); // ProfileService의 uploadDir 수동 설정
        // Mocking the repository's save method
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testUploadProfilePicture() throws IOException {
        // Arrange
        Path imagePath = Paths.get("tree.jpg"); // 테스트할 이미지 파일 경로
        byte[] imageBytes = Files.readAllBytes(imagePath); // 이미지 파일을 바이트 배열로 변환
        MultipartFile mockFile = new MockMultipartFile(
                "file", "tree.jpg",
                "image/jpeg", imageBytes); // MockMultipartFile 생성

        Profile profile = new Profile(); // 새 프로필 객체 생성

        // Act
        Profile savedProfile = profileService.uploadProfilePicture(mockFile, profile); // 이미지 업로드 메서드 호출

        // Assert
        assertEquals(savedProfile.getRealName(), "tree.jpg"); // 원래 파일 이름이 올바른지 확인
        assertTrue(savedProfile.getServerName().contains("tree.jpg")); // serverName에 파일 이름이 포함되어 있는지 확인
        assertEquals(savedProfile.getFileSize(), (int) mockFile.getSize()); // 파일 크기가 올바른지 확인
        assertEquals(savedProfile.getMineType(), mockFile.getContentType()); // MIME 타입이 올바른지 확인
        assertTrue(Files.exists(Paths.get(uploadDir, savedProfile.getServerName()))); // 파일이 실제로 업로드 되었는지 확인
    }
}
