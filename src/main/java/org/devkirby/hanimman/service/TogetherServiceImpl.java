package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.Together;
import org.devkirby.hanimman.entity.TogetherImage;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.AddressRepository;
import org.devkirby.hanimman.repository.TogetherFavoriteRepository;
import org.devkirby.hanimman.repository.TogetherImageRepository;
import org.devkirby.hanimman.repository.TogetherRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final TogetherImageRepository togetherImageRepository;
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final TogetherImageService togetherImageService;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(TogetherServiceImpl.class);
    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public void create(TogetherDTO togetherDTO) throws IOException {
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherDTO.setId(togetherRepository.save(together).getId());
        togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getId());
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
        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
        togetherDTO.setAddress(address.get()
                .getCityName() + " " + address.get().getDistrictName() + " " +
                address.get().getNeighborhoodName());
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
    public Page<TogetherDTO> listAll(Pageable pageable, Boolean isEnd, String sortBy) {
        if (sortBy.equals("meetingAt")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(isEnd){
            log.info("isEnd is true");
            return togetherRepository.findByIsEndIsFalse(pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageUrls(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
                        togetherDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
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
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
                        togetherDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }
    }

    @Override
    public Page<TogetherDTO> searchByKeywords(String keyword, Pageable pageable, Boolean isEnd, String sortBy) {
        if (sortBy.equals("meetingAt")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(isEnd){
            return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(
                            keyword, keyword, pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageUrls(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
                        togetherDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }else{
            return togetherRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageUrls(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
                        togetherDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }

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
                .map(togetherImage -> "http://localhost:8080/uploads/" + togetherImage.getServerName())
                .collect(Collectors.toList());
        if (imageUrls.isEmpty()) {
            imageUrls.add("http://localhost:8080/"+defaultImageUrl);
        }
        return imageUrls;
    }

    private List<String> getImageThumbnailUrls(Together together){
        List<String> imageUrls = togetherImageRepository.findByParentAndDeletedAtIsNull(together)
                .stream()
                .map(togetherimage -> "http://localhost:8080/uploads/t_" + togetherimage.getServerName())
                .findFirst()
                .map(List::of)
                .orElseGet(() -> List.of("http://localhost:8080/"+defaultImageUrl));
        return imageUrls;
    }
}