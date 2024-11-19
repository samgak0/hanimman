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

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final TogetherImageRepository togetherImageRepository;
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final TogetherImageService togetherImageService;
    private final ModelMapper modelMapper;

    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public void create(TogetherDTO togetherDTO) throws IOException {
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherRepository.save(together);
        togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getUserId());
    }

    @Override
    public TogetherDTO read(Integer id, User loginUser) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 같이가요 게시글이 없습니다. : " + id));

        //조회수 증가
        Integer view = together.getViews() + 1;
        together.setViews(view);
        togetherRepository.save(together);

        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
        togetherDTO.setImageUrls(getImageUrls(together));

        boolean isFavorite = togetherFavoriteRepository.existsByUserAndParent(loginUser, together);
        togetherDTO.setFavorite(isFavorite);
        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
        togetherDTO.setFavoriteCount(favoriteCount);

        return togetherDTO;
    }

    @Override
    @Transactional
    public void update(TogetherDTO togetherDTO) throws IOException {
        Together existingTogether = togetherRepository.findById(togetherDTO.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 같이가요 게시글이 없습니다. : " + togetherDTO.getId()));
        togetherImageService.deleteByParent(togetherDTO.getId());
        existingTogether.setModifiedAt(Instant.now());
        togetherRepository.save(existingTogether);

        togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getUserId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다. : " + id));
        together.setDeletedAt(Instant.now());
        togetherRepository.save(together);
    }

    @Override
    public Page<TogetherDTO> listAll(Pageable pageable, Boolean isEnd) {
        Instant now = Instant.now();
        if(!isEnd){
            return togetherRepository.findByIsEndIsFalse(pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageUrls(getImageThumbnailUrls(together));

                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }
        else{
            return togetherRepository.findAll(pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageUrls(getImageThumbnailUrls(together));

                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }
    }

    @Override
    public Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable) {
        Instant now = Instant.now();
        return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    togetherDTO.setImageUrls(getImageThumbnailUrls(together));

                    Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                    togetherDTO.setFavoriteCount(favoriteCount);
                    return togetherDTO;
                });
    }

    @Override
    @Transactional
    public void updateIsEnd() {
        List<Together> togethers = togetherRepository.findByIsEndIsFalse();
        Instant now = Instant.now();
        togethers.forEach(together -> {
            if(together.getMeetingAt().isBefore(now)){
                together.setIsEnd(true);
                togetherRepository.save(together);
            }
        });

    }
/*
    @Override
    public Page<TogetherDTO> listNotEnd(Pageable pageable) {
        return togetherRepository.findByIsEndIsFalse(pageable)
                .map(together -> {
                    TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                    togetherDTO.setImageUrls(getImageUrls(together));

                    Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                    togetherDTO.setFavoriteCount(favoriteCount);
                    return togetherDTO;
                });
    }

 */

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

    private List<String> getImageThumbnailUrls(Together together){
        List<String> imageUrls = togetherImageRepository.findByParentAndDeletedAtIsNull(together)
                .stream()
                .map(togetherimage -> "t_" + togetherimage.getServerName())
                .findFirst()
                .map(List::of)
                .orElseGet(() -> List.of(defaultImageUrl));
        return imageUrls;
    }
}