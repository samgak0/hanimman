package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
public class TogetherImageServiceImpl implements TogetherImageService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final TogetherImageRepository togetherImageRepository;
    private final TogetherRepository togetherRepository;
    private final ModelMapper modelMapper;

    public TogetherImageServiceImpl(TogetherImageRepository togetherImageRepository, TogetherRepository togetherRepository, ModelMapper modelMapper) {
        this.togetherImageRepository = togetherImageRepository;
        this.togetherRepository = togetherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void create(TogetherImageDTO togetherImageDTO) {
        TogetherImage togetherImage = modelMapper.map(togetherImageDTO, TogetherImage.class);
        TogetherImage result = togetherImageRepository.save(togetherImage);
    }

    @Override
    public TogetherImageDTO read(Integer id) {
        TogetherImage result = togetherImageRepository.findById(id).orElseThrow();
        TogetherImageDTO togetherImageDTO = modelMapper.map(result, TogetherImageDTO.class);
        return togetherImageDTO;
    }

    @Override
    @Transactional
    public void update(TogetherImageDTO togetherImageDTO) {
        TogetherImage togetherImage = modelMapper.map(togetherImageDTO, TogetherImage.class);
        togetherImageRepository.save(togetherImage);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        TogetherImage togetherImage = togetherImageRepository.findById(id).orElseThrow();
        togetherImage.setDeletedAt(Instant.now());
        togetherImageRepository.save(togetherImage);
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile file, Integer togetherId) throws IOException {
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
        Together together = togetherRepository.findById(togetherId).orElseThrow(() -> new IllegalArgumentException("Invalid together ID"));

        // Create and save the ShareImage entity
        TogetherImage togetherImage = TogetherImage.builder()
                .originalName(originalName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int) file.getSize())
                .parent(together)
                .user(together.getUser())
                .build();

        togetherImageRepository.save(togetherImage);

        return "File uploaded successfully " + serverName;
    }
}
