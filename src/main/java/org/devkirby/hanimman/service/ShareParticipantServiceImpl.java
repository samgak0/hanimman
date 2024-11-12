package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareParticipantDTO;
import org.devkirby.hanimman.entity.ShareParticipant;
import org.devkirby.hanimman.repository.ShareParticipantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ShareParticipantServiceImpl implements ShareParticipantService {
    private final ShareParticipantRepository shareParticipantRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(ShareParticipantDTO shareParticipantDTO) {
        ShareParticipant shareParticipant = modelMapper.map(shareParticipantDTO, ShareParticipant.class);
        ShareParticipant result = shareParticipantRepository.save(shareParticipant);
    }

    @Override
    public ShareParticipantDTO read(Integer id) {
        ShareParticipant result = shareParticipantRepository.findById(id).orElseThrow();
        return modelMapper.map(result, ShareParticipantDTO.class);
    }

    @Override
    @Transactional
    public void update(ShareParticipantDTO shareParticipantDTO) {
        ShareParticipant shareParticipant = modelMapper.map(shareParticipantDTO, ShareParticipant.class);
        ShareParticipant result = shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id).orElseThrow();
        shareParticipant.setDeletedAt(Instant.now());
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void rejected(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id).orElseThrow();
        shareParticipant.setRejected(true);
        shareParticipantRepository.save(shareParticipant);
    }

}
