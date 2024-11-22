package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherParticipantDTO;
import org.devkirby.hanimman.entity.TogetherParticipant;
import org.devkirby.hanimman.repository.TogetherParticipantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TogetherParticipantServiceImpl implements TogetherParticipantService {
    private final TogetherParticipantRepository togetherParticipantRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(TogetherParticipantDTO togetherParticipantDTO) {
        TogetherParticipant togetherParticipant = modelMapper.map(togetherParticipantDTO, TogetherParticipant.class);
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    public TogetherParticipantDTO read(Integer id) {
        TogetherParticipant result = togetherParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        return modelMapper.map(result, TogetherParticipantDTO.class);
    }

    @Override
    @Transactional
    public void update(TogetherParticipantDTO togetherParticipantDTO) {
        togetherParticipantRepository.findById(togetherParticipantDTO.getId())
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        TogetherParticipant togetherParticipant = modelMapper.map(togetherParticipantDTO, TogetherParticipant.class);
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        togetherParticipant.setDeletedAt(Instant.now());
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    @Transactional
    public void rejected(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        togetherParticipant.setRejected(Instant.now());
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    @Transactional
    public void accepted(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        togetherParticipant.setAccepted(Instant.now());
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    @Transactional
    public void complete(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 참여자가 존재하지 않습니다."));
        togetherParticipant.setComplete(Instant.now());
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    public List<TogetherParticipantDTO> listAllByParentId(Integer parentId) {
        return togetherParticipantRepository.findByParentId(parentId).stream()
                .map(togetherParticipant -> modelMapper.map(togetherParticipant, TogetherParticipantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TogetherParticipantDTO> listAllByParentIdAndRejectedIsNull(Integer parentId) {
        return togetherParticipantRepository.findByParentIdAndRejectedIsNull(parentId).stream()
                .map(togetherParticipant -> modelMapper.map(togetherParticipant, TogetherParticipantDTO.class))
                .collect(Collectors.toList());
    }
}
