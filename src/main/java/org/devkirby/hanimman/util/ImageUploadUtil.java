package org.devkirby.hanimman.util;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageUploadUtil {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadDir);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
        uploadDir = tempFolder.getAbsolutePath();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        String originalName = file.getOriginalFilename();
        String serverName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = Paths.get(uploadDir).resolve(serverName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            createThumbnail(targetPath, serverName);
        } catch (IOException e) {
            throw new IOException("이미지 업로드에 실패했습니다.", e);
        }

        return  serverName;
    }

    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> uploadFileNames = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                uploadFileNames.add(uploadImage(file));
            }
        }
        return uploadFileNames;
    }

    private void createThumbnail(Path imagePath, String serverName) throws IOException {
        String thumbnailName = "t_" + serverName;
        Path thumbnailPath = Paths.get(uploadDir).resolve(thumbnailName);

        Thumbnails.of(imagePath.toFile())
                .size(200, 200)
                .toFile(thumbnailPath.toFile());
    }

    public void deleteImage(String serverName) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(serverName);
        Path thumbnailPath = Paths.get(uploadDir).resolve("t_" + serverName);

        try {
            Files.deleteIfExists(imagePath);
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException e) {
            throw new IOException("이미지 삭제에 실패했습니다.", e);
        }
    }
}