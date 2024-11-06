package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
