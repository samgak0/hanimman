package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.devkirby.hanimman.util.ImageUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TogetherImageServiceImpl implements TogetherImageService {
    private final TogetherImageRepository togetherImageRepository;
    private final TogetherRepository togetherRepository;
    private final ImageUploadUtil imageUploadUtil;
    private final ModelMapper modelMapper;

    public String uploadDir;

    @Override
    @Transactional
    public void create(TogetherImageDTO togetherImageDTO) {
        TogetherImage togetherImage = modelMapper.map(togetherImageDTO, TogetherImage.class);
        togetherImageRepository.save(togetherImage);
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
    public void deleteByParent(Integer togetherId) {
        List<TogetherImage> togetherImages = togetherImageRepository.findAllByParentId(togetherId);
        for (TogetherImage togetherImage : togetherImages) {
            togetherImage.setDeletedAt(Instant.now());
            togetherImageRepository.save(togetherImage);
        }
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile file, Integer togetherId) throws IOException {
        String serverName = imageUploadUtil.uploadImage(file);
        Together together = togetherRepository.findById(togetherId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 함께요 게시글이 없습니다 : " + togetherId));

        TogetherImage togetherImage = TogetherImage.builder()
                .originalName(file.getOriginalFilename())
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int) file.getSize())
                .parent(together)
                .user(together.getUser())
                .build();

        togetherImageRepository.save(togetherImage);

        return serverName;
    }

    @Override
    @Transactional
    public List<String> uploadImages(List<MultipartFile> multipartFiles, Integer togetherId) throws IOException {
        List<String> serverNames = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            serverNames.add(uploadImage(file, togetherId));
        }
        return serverNames;
    }
}
