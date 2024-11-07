package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareImageServiceImpl implements ShareImageService{
    @Autowired
    private final ShareImageRepository shareImageRepository;

    @Autowired
    private ShareRepository shareRepository;

    private final ModelMapper modelMapper;

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
        String uploadDir = "shareImages/";
        File uploadFile = new File(uploadDir);
        if(!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String serverName = UUID.randomUUID().toString() + "_" + originalName;

        Path targetPath = new File(uploadDir + serverName).toPath();

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Share share = shareRepository.findById(shareId).orElseThrow();

        ShareImage shareImage = ShareImage.builder()
                .originalName(originalName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int) file.getSize())
                .parent(share)
                .user(share.getUser())
                .build();

        shareImageRepository.save(shareImage);

        return "File uploaded successfully" + serverName;
    }
}
