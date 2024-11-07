package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private final ShareRepository shareRepository;
    private final ModelMapper modelMapper;

    @Override
    public Integer register(ShareDTO shareDTO) {
        Share share = modelMapper.map(shareDTO, Share.class);
        Share result = shareRepository.save(share);
        return result.getId();
    }

    @Override
    public ShareDTO get(Integer id) {
        Share result = shareRepository.findById(id).orElseThrow();
        return modelMapper.map(result, ShareDTO.class);
    }

    @Override
    public void modify(ShareDTO shareDTO) {
        shareDTO.setModifiedAt(Instant.now());
        Share share = modelMapper.map(shareDTO, Share.class);
        shareRepository.save(share);
    }

    @Override
    public void remove(Integer id) {
        Share share = shareRepository.findById(id).orElseThrow();
        share.setDeletedAt(Instant.now());
        shareRepository.save(share);
    }
}
