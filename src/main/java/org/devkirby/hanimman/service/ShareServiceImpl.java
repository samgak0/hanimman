package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.repository.ShareImageRepository;
import org.devkirby.hanimman.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private final ShareRepository shareRepository;
    private final ShareImageRepository shareImageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(ShareDTO shareDTO, ShareImageDTO shareImageDTO) {
        Share share = modelMapper.map(shareDTO, Share.class);
        ShareImage shareImage = modelMapper.map(shareImageDTO, ShareImage.class);
        shareRepository.save(share);
        shareImage.setParent(share);
        shareImageRepository.save(shareImage);
    }

    @Override
    public ShareDTO read(Integer id) {
        Share share = shareRepository.findById(id).orElseThrow();
        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
        List<ShareImage> shareImages = shareImageRepository.findByParentAndDeletedAtIsNull(share);
        List<String> imageUrls = shareImages.stream()
                .map(ShareImage::getServerName)
                .collect(Collectors.toList());
        shareDTO.setImageUrls(imageUrls);
        return shareDTO;
    }

    @Override
    @Transactional
    public void update(ShareDTO shareDTO) {
        shareDTO.setModifiedAt(Instant.now());
        Share share = modelMapper.map(shareDTO, Share.class);
        shareRepository.save(share);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Share share = shareRepository.findById(id).orElseThrow();
        share.setDeletedAt(Instant.now());
        shareRepository.save(share);
    }

    @Override
    public Page<ShareDTO> listAll(Pageable pageable) {
        return shareRepository.findAll(pageable)
                .map(share -> {
                    ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                    List<ShareImage> shareImages = shareImageRepository.findByParentAndDeletedAtIsNull(share);
                    List<String> imageUrls = shareImages.stream()
                            .map(ShareImage::getServerName)
                            .collect(Collectors.toList());
                    shareDTO.setImageUrls(imageUrls);
                    return shareDTO;
                });
    }

    @Override
    public Page<ShareDTO> searchByKeywords(String keyword, Pageable pageable) {
        return shareRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(share -> {
                    ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                    List<ShareImage> shareImages = shareImageRepository.findByParentAndDeletedAtIsNull(share);
                    List<String> imageUrls = shareImages.stream()
                            .map(ShareImage::getServerName)
                            .collect(Collectors.toList());
                    shareDTO.setImageUrls(imageUrls);
                    return shareDTO;
                });
    }
}
