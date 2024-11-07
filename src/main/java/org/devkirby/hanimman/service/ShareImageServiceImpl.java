package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.repository.ShareImageRepository;
import org.devkirby.hanimman.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

@Service
public class ShareImageServiceImpl implements ShareImageService{
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final ShareImageRepository shareImageRepository;
    private final ShareRepository shareRepository;
    private final ModelMapper modelMapper;

    public ShareImageServiceImpl(ShareImageRepository shareImageRepository, ShareRepository shareRepository, ModelMapper modelMapper) {
        this.shareImageRepository = shareImageRepository;
        this.shareRepository = shareRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void create(ShareImageDTO shareImageDTO) {
        ShareImage shareImage = modelMapper.map(shareImageDTO, ShareImage.class);
        ShareImage result = shareImageRepository.save(shareImage);
    }

    @Override
    public ShareImageDTO read(Integer id) {
        ShareImage result = shareImageRepository.findById(id).orElseThrow();
        ShareImageDTO shareImageDTO = modelMapper.map(result, ShareImageDTO.class);
        return shareImageDTO;
    }

    @Override
    public void update(ShareImageDTO shareImageDTO) {
        ShareImage shareImage = modelMapper.map(shareImageDTO, ShareImage.class);
        shareImageRepository.save(shareImage);
    }

    @Override
    public void delete(Integer id) {
        ShareImage shareImage = shareImageRepository.findById(id).orElseThrow();
        shareImage.setDeletedAt(Instant.now());
        shareImageRepository.save(shareImage);
    }


    @Override
    public String uploadImage(MultipartFile file, Integer shareId) throws IOException {
        // Ensure the upload directory exists
        File uploadFile = new File(uploadDir);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        // Generate a unique file name
        String originalName = file.getOriginalFilename();
        String serverName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = Paths.get(uploadDir).resolve(serverName);

        // Save the file to the target path
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to save file", e);
        }

        // Retrieve the share entity
        Share share = shareRepository.findById(shareId).orElseThrow(() -> new IllegalArgumentException("Invalid share ID"));

        // Create and save the ShareImage entity
        ShareImage shareImage = ShareImage.builder()
                .originalName(originalName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int) file.getSize())
                .parent(share)
                .user(share.getUser())
                .build();

        shareImageRepository.save(shareImage);

        return "File uploaded successfully " + serverName;
    }
}
