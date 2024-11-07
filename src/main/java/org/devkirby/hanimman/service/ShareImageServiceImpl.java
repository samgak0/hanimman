package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareImageDTO;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.repository.ShareImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ShareImageServiceImpl implements ShareImageService{
    private final ShareImageRepository shareImageRepository;
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
}
