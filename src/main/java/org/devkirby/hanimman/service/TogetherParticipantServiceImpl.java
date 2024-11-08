package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherParticipantDTO;
import org.devkirby.hanimman.entity.TogetherParticipant;
import org.devkirby.hanimman.repository.TogetherParticipantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TogetherParticipantServiceImpl implements TogetherParticipantService {
    private final TogetherParticipantRepository togetherParticipantRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(TogetherParticipantDTO togetherParticipantDTO) {
        TogetherParticipant togetherParticipant = modelMapper.map(togetherParticipantDTO, TogetherParticipant.class);
        TogetherParticipant result = togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    public TogetherParticipantDTO read(Integer id) {
        TogetherParticipant result = togetherParticipantRepository.findById(id).orElseThrow();
        return modelMapper.map(result, TogetherParticipantDTO.class);
    }

    @Override
    public void update(TogetherParticipantDTO togetherParticipantDTO) {
        TogetherParticipant togetherParticipant = modelMapper.map(togetherParticipantDTO, TogetherParticipant.class);
        TogetherParticipant result = togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    public void delete(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id).orElseThrow();
        togetherParticipant.setDeletedAt(Instant.now());
        togetherParticipantRepository.save(togetherParticipant);
    }

    @Override
    public void rejected(Integer id) {
        TogetherParticipant togetherParticipant = togetherParticipantRepository.findById(id).orElseThrow();
        togetherParticipant.setRejected(true);
        togetherParticipantRepository.save(togetherParticipant);
    }
}
