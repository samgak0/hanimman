package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherFavoriteDTO;
import org.devkirby.hanimman.entity.*;
import org.devkirby.hanimman.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final TogetherImageRepository togetherImageRepository;
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final TogetherImageService togetherImageService;
    private final TogetherParticipantRepository togetherParticipantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(TogetherServiceImpl.class);
    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public void create(TogetherDTO togetherDTO, String primaryAddressId) throws IOException {
        Optional<User> user = userRepository.findById(togetherDTO.getUserId());
        togetherDTO.setAddressId(primaryAddressId);
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherDTO.setId(togetherRepository.save(together).getId());
        if(togetherDTO.getFiles() != null && !togetherDTO.getFiles().isEmpty()){
            togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getId());
        }
    }

    @Override
    public TogetherDTO read(Integer id, CustomUserDetails loginUser) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 같이가요 게시글이 없습니다. : " + id));

        //조회수 증가
        Integer view = together.getViews() + 1;
        together.setViews(view);
        togetherRepository.save(together);
        User user = modelMapper.map(loginUser, User.class);
        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);

        //작성자 판별
        if(Objects.equals(together.getUser().getId(), loginUser.getId())){
            togetherDTO.setWriter(true);
        }else{
            togetherDTO.setWriter(false);
        }

        //참여 신청 여부 판별'
        if(togetherParticipantRepository.existsByUserIdAndParentId(user.getId(), together.getId())) {
            togetherDTO.setParticipant(true);
        }else {
            togetherDTO.setParticipant(false);
        }

        togetherDTO.setImageIds(getImageUrls(together));
        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
        togetherDTO.setAddress(address.get()
                .getCityName() + " " + address.get().getDistrictName() + " " +
                address.get().getNeighborhoodName());
        boolean isFavorite = togetherFavoriteRepository.existsByUserAndParent(user, together);
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
                    pageable.getPageSize(), Sort.by(Sort.Order.asc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(!isEnd){
            log.info("isEnd is true");
            return togetherRepository.findByIsEndIsFalseAndDeletedAtIsNull(pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageIds(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//                        togetherDTO.setAddress(address.get()
//                                .getCityName() + " " + address.get().getDistrictName() + " " +
//                                address.get().getNeighborhoodName());
                        togetherDTO.setAddress(address.get().getNeighborhoodName());
                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }
        else{
            return togetherRepository.findByDeletedAtIsNull(pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageIds(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//                        togetherDTO.setAddress(address.get()
//                                .getCityName() + " " + address.get().getDistrictName() + " " +
//                                address.get().getNeighborhoodName());
                        togetherDTO.setAddress(address.get().getNeighborhoodName());
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
                    pageable.getPageSize(), Sort.by(Sort.Order.asc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(isEnd){
            return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(
                            keyword, keyword, pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageIds(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//                        togetherDTO.setAddress(address.get()
//                                .getCityName() + " " + address.get().getDistrictName() + " " +
//                                address.get().getNeighborhoodName());
                        togetherDTO.setAddress(address.get().getNeighborhoodName());
                        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
                        togetherDTO.setFavoriteCount(favoriteCount);
                        return togetherDTO;
                    });
        }else{
            return togetherRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                    .map(together -> {
                        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
                        togetherDTO.setImageIds(getImageThumbnailUrls(together));
                        Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//                        togetherDTO.setAddress(address.get()
//                                .getCityName() + " " + address.get().getDistrictName() + " " +
//                                address.get().getNeighborhoodName());
                        togetherDTO.setAddress(address.get().getNeighborhoodName());
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
            Instant meetingAt = together.getMeetingAt();
            if(meetingAt != null && together.getMeetingAt().isBefore(now)){
                together.setIsEnd(true);
                togetherRepository.save(together);
            }
        });
    }

    @Override
    public Page<TogetherDTO> listByUserIdFavorite(Integer userId, Pageable pageable) {
        List<TogetherFavorite> togetherFavorites = togetherFavoriteRepository.findByUserId(userId);
        List<Together> togethers = togetherFavorites.stream()
                .map(togetherFavorite -> togetherFavorite.getParent())
                .collect(Collectors.toList());
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));

        Page<Together> togetherPage = new PageImpl<>(togethers, pageable, togethers.size());
        return togetherPage.map(together -> {
            TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
            togetherDTO.setImageIds(getImageUrls(together));
            Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//            togetherDTO.setAddress(address.get()
//                    .getCityName() + " " + address.get().getDistrictName() + " " +
//                    address.get().getNeighborhoodName());
            togetherDTO.setAddress(address.get().getNeighborhoodName());
            Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
            togetherDTO.setFavoriteCount(favoriteCount);
            return togetherDTO;
        });
    }
    @Override
    @Transactional
    public File downloadImage(Integer id) throws IOException {
        TogetherImage togetherImage = togetherImageRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 이미지가 없습니다. : " + id));
        File file = new File("C:/upload/" + togetherImage.getServerName());

        return file;
    }

    @Override
    public Page<TogetherDTO> listByUserId(Integer userId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<Together> togetherPage = togetherRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        return togetherPage.map(together -> {
            TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
            togetherDTO.setImageIds(getImageUrls(together));
            Optional<Address> address = addressRepository.findById(togetherDTO.getAddressId());
//            togetherDTO.setAddress(address.get()
//                    .getCityName() + " " + address.get().getDistrictName() + " " +
//                    address.get().getNeighborhoodName());
            togetherDTO.setAddress(address.get().getNeighborhoodName());
            Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
            togetherDTO.setFavoriteCount(favoriteCount);
            return togetherDTO;
        });
    }

    private List<Integer> getImageUrls(Together together) {
        List<Integer> imageUrls = togetherImageRepository.findByParentAndDeletedAtIsNull(together)
                .stream()
                .map(togetherImage -> togetherImage.getId())
                .collect(Collectors.toList());

        return imageUrls;
    }


    private List<Integer> getImageThumbnailUrls(Together together){
        List<Integer> imageIds = togetherImageRepository.findByParentAndDeletedAtIsNull(together)
                .stream()
                .map(togetherimage -> togetherimage.getId())
                .findFirst()
                .map(List::of).orElse(List.of(0));

        return imageIds;
    }
}