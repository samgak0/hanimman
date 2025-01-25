package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareParticipantDTO;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareParticipant;
import org.devkirby.hanimman.repository.ShareImageRepository;
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
    private final ShareImageRepository shareImageRepository;
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
        shareParticipant.setRejected(Instant.now());
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void accepted(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        shareParticipant.setAccepted(Instant.now());
        shareParticipantRepository.save(shareParticipant);
    }

    @Override
    @Transactional
    public void complete(Integer id) {
        ShareParticipant shareParticipant = shareParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        shareParticipant.setComplete(Instant.now());
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
    public List<ShareParticipantDTO> listAllByParentIdAndRejectedIsNull(Integer parentId) {
        List<ShareParticipant> shareParticipants = shareParticipantRepository.findByParentIdAndRejectedIsNull(parentId);
        return shareParticipants.stream()
                .map(shareParticipant -> modelMapper.map(shareParticipant, ShareParticipantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShareParticipantDTO> listAllByUserId(Integer userId) {
        List<ShareParticipant> shareParticipants = shareParticipantRepository.findByUserId(userId);
        return shareParticipants.stream()
                .map(shareParticipant -> {
                    ShareParticipantDTO shareParticipantDTO = modelMapper.map(shareParticipant, ShareParticipantDTO.class);
                    shareParticipantDTO.setTitle(shareParticipant.getParent().getTitle());
                    shareParticipantDTO.setNickname(shareParticipant.getParent().getUser().getNickname());
                    shareParticipantDTO.setImageIds( getImageThumbnailUrls(shareParticipant.getParent()));
                    return shareParticipantDTO;
                })
                .collect(Collectors.toList());
    }

    private List<Integer> getImageThumbnailUrls(Share share){
        List<Integer> imageIds = shareImageRepository.findByParentAndDeletedAtIsNull(share)
                .stream()
                .map(shareImage -> shareImage.getId())
                .findFirst()
                .map(List::of).orElse(List.of(0));

        return imageIds;
    }
}
