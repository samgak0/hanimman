package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareParticipantDTO;
import org.devkirby.hanimman.entity.ShareParticipant;
import org.devkirby.hanimman.repository.ShareParticipantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareParticipantServiceImpl implements ShareParticipantService {
    private final ShareParticipantRepository shareParticipantRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(ShareParticipantDTO shareParticipantDTO) {
        ShareParticipant shareParticipant = modelMapper.map(shareParticipantDTO, ShareParticipant.class);
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    public ShareParticipantDTO read(Integer id) {
        ShareParticipant result = shareParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        return modelMapper.map(result, ShareParticipantDTO.class);
    }

    @Override
    @Transactional
    public void update(ShareParticipantDTO shareParticipantDTO) {
        shareParticipantRepository.findById(shareParticipantDTO.getId())
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        ShareParticipant shareParticipant = modelMapper.map(shareParticipantDTO, ShareParticipant.class);
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        shareParticipant.setDeletedAt(Instant.now());
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void rejected(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        shareParticipant.setRejected(true);
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    public List<ShareParticipantDTO> listAllByParentId(Integer parentId) {
        List<ShareParticipant> shareParticipants = shareParticipantRepository.findByParentId(parentId);
        return shareParticipants.stream()
                .map(shareParticipant -> modelMapper.map(shareParticipant, ShareParticipantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShareParticipantDTO> listAllByParentIdAndRejectedIsFalse(Integer parentId) {
        List<ShareParticipant> shareParticipants = shareParticipantRepository.findByParentIdAndRejectedIsFalse(parentId);
        return shareParticipants.stream()
                .map(shareParticipant -> modelMapper.map(shareParticipant, ShareParticipantDTO.class))
                .collect(Collectors.toList());
    }
}
