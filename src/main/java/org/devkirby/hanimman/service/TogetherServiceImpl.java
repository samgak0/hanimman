package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final ModelMapper modelMapper;

    @Override
    public Integer register(TogetherDTO togetherDTO) {
        Together together = modelMapper.map(togetherDTO, Together.class);
        Together result = togetherRepository.save(together);
        return result.getId();
    }

    @Override
    public TogetherDTO get(Integer id) {
        Together result = togetherRepository.findById(id).orElseThrow();
        return modelMapper.map(result, TogetherDTO.class);
    }

    @Override
    public void modify(TogetherDTO togetherDTO) {
        togetherDTO.setModifiedAt(Instant.now());
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherRepository.save(together);
    }

    @Override
    public void remove(Integer id) {
        Together together = togetherRepository.findById(id).orElseThrow();
        together.setDeletedAt(Instant.now());
        togetherRepository.save(together);
    }

}
