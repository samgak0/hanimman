package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherImageDTO;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.TogetherFavoriteRepository;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final ModelMapper modelMapper;

    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public void create(TogetherDTO togetherDTO, TogetherImageDTO togetherImageDTO) {
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherRepository.save(together);
    }

    @Override
    public TogetherDTO read(Integer id, User loginUser) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Together not found with id: " + id));
        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
        togetherDTO.setImageUrls(getImageUrls(together));

        boolean isFavorite = togetherFavoriteRepository.existsByUserAndParent(loginUser, together);
        togetherDTO.setFavorite(isFavorite);

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
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Together not found with id: " + id));
        together.setDeletedAt(Instant.now());
        togetherRepository.save(together);
    }

    @Override
    public Page<TogetherDTO> listAll(Pageable pageable) {
        return togetherRepository.findAll(pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    togetherDTO.setImageUrls(getImageUrls(together));
                    return togetherDTO;
                });
    }

    @Override
    public Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable) {
        return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    togetherDTO.setImageUrls(getImageUrls(together));
                    return togetherDTO;
                });
    }

    private List<String> getImageUrls(Together together) {
        List<String> imageUrls = togetherImageRepository.findByParentAndDeletedAtIsNull(together)
                .stream()
                .map(TogetherImage::getServerName)
                .collect(Collectors.toList());
        if (imageUrls.isEmpty()) {
            imageUrls.add(defaultImageUrl);
        }
        return imageUrls;
    }
}