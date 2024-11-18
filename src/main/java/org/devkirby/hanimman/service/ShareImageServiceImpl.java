package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.repository.ShareImageRepository;
import org.devkirby.hanimman.repository.ShareRepository;
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
public class ShareImageServiceImpl implements ShareImageService {
    private final ShareImageRepository shareImageRepository;
    private final ShareRepository shareRepository;
    private final ModelMapper modelMapper;
    private final ImageUploadUtil imageUploadUtil;

    public String uploadDir;
    @Override
    @Transactional
    public void create(ShareImageDTO shareImageDTO) {
        ShareImage shareImage = modelMapper.map(shareImageDTO, ShareImage.class);
        shareImageRepository.save(shareImage);
    }

    @Override
    public ShareImageDTO read(Integer id) {
        ShareImage shareImage = shareImageRepository.findById(id).orElseThrow();
        return modelMapper.map(shareImage, ShareImageDTO.class);
    }

    @Override
    @Transactional
    public void update(ShareImageDTO shareImageDTO) {
        ShareImage shareImage = modelMapper.map(shareImageDTO, ShareImage.class);
        shareImageRepository.save(shareImage);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ShareImage shareImage = shareImageRepository.findById(id).orElseThrow();
        shareImage.setDeletedAt(Instant.now());
        shareImageRepository.save(shareImage);
    }

    @Override
    @Transactional
    public void deleteByParent(Integer shareId) {
        List<ShareImage> shareImages = shareImageRepository.findAllByParentId(shareId);
        for(ShareImage shareImage : shareImages){
            shareImage.setDeletedAt(Instant.now());
            shareImageRepository.save(shareImage);
        }
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile file, Integer shareId) throws IOException {
        // Share 엔티티 조회
        Share share = shareRepository.findById(shareId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나눠요 게시글 ID 입니다."));
        // 이미지 업로드
        String serverName = imageUploadUtil.uploadImage(file);

        // ShareImage 엔티티 생성 및 저장
        ShareImage shareImage = ShareImage.builder()
                .originalName(file.getOriginalFilename())
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int) file.getSize())
                .parent(share)
                .user(share.getUser())
                .build();

        shareImageRepository.save(shareImage);

        return  serverName;
    }

    @Override
    @Transactional
    public List<String> uploadImages(List<MultipartFile> multipartFiles, Integer shareId) throws IOException{
        List<String> serverNames = new ArrayList<>();
        for(MultipartFile file : multipartFiles){
            serverNames.add(uploadImage(file, shareId));
        }
        return serverNames;
    }
}