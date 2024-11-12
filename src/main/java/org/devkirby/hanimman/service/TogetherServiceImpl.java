package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final TogetherImageRepository togetherImageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(TogetherDTO togetherDTO, TogetherImageDTO togetherImageDTO) {
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherRepository.save(together);
    }

    @Override
    public TogetherDTO read(Integer id) {
        Together result = togetherRepository.findById(id).orElseThrow();
        TogetherDTO togetherDTO = modelMapper.map(result, TogetherDTO.class);
        List<TogetherImage> togetherImages = togetherImageRepository.findByParentAndDeletedAtIsNull(result);
        List<String> imageUrls = togetherImages.stream()
                .map(TogetherImage::getServerName)
                .collect(Collectors.toList());
        togetherDTO.setImageUrls(imageUrls);
        return togetherDTO;
    }

    @Override
    @Transactional
    public void update(TogetherDTO togetherDTO) {
        togetherDTO.setModifiedAt(Instant.now());
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherRepository.save(together);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Together together = togetherRepository.findById(id).orElseThrow();
        together.setDeletedAt(Instant.now());
        togetherRepository.save(together);
    }

    @Override
    public Page<TogetherDTO> listAll(Pageable pageable) {
        return togetherRepository.findAll(pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    List<TogetherImage> togetherImages = togetherImageRepository.findByParentAndDeletedAtIsNull(together);
                    List<String> imageUrls = togetherImages.stream()
                            .map(TogetherImage::getServerName)
                            .collect(Collectors.toList());
                    togetherDTO.setImageUrls(imageUrls);
                    return togetherDTO;
                });
    }

    @Override
    public Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable) {
        return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    List<TogetherImage> togetherImages = togetherImageRepository.findByParentAndDeletedAtIsNull(together);
                    List<String> imageUrls = togetherImages.stream()
                            .map(TogetherImage::getServerName)
                            .collect(Collectors.toList());
                    togetherDTO.setImageUrls(imageUrls);
                    return togetherDTO;
                });
    }
}
