package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TogetherImageServiceImpl implements TogetherImageService {
    private final TogetherImageRepository togetherImageRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(TogetherImageDTO togetherImageDTO) {
        TogetherImage togetherImage = modelMapper.map(togetherImageDTO, TogetherImage.class);
        TogetherImage result = togetherImageRepository.save(togetherImage);
    }

    @Override
    public TogetherImageDTO read(Integer id) {
        TogetherImage result = togetherImageRepository.findById(id).orElseThrow();
        TogetherImageDTO togetherImageDTO = modelMapper.map(result, TogetherImageDTO.class);
        return togetherImageDTO;
    }

    @Override
    public void update(TogetherImageDTO togetherImageDTO) {
        TogetherImage togetherImage = modelMapper.map(togetherImageDTO, TogetherImage.class);
        togetherImageRepository.save(togetherImage);
    }

    @Override
    public void delete(Integer id) {
        TogetherImage togetherImage = togetherImageRepository.findById(id).orElseThrow();
        togetherImage.setDeletedAt(Instant.now());
        togetherImageRepository.save(togetherImage);
    }
}
