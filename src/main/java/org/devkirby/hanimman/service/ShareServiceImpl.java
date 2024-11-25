package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.AddressDTO;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.entity.Share;
import org.devkirby.hanimman.entity.ShareImage;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.*;
import org.modelmapper.ModelMapper;
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
public class ShareServiceImpl implements ShareService {
    private final ShareRepository shareRepository;
    private final ShareImageRepository shareImageRepository;
    private final ShareFavoriteRepository shareFavoriteRepository;
    private final ShareImageService shareImageService;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public void create(ShareDTO shareDTO) throws IOException {
        Share share = modelMapper.map(shareDTO, Share.class);
        shareRepository.save(share);
        shareImageService.uploadImages(shareDTO.getFiles(), shareDTO.getUserId());
    }

    @Override
    public ShareDTO read(Integer id, User loginUser) {
        Share share = shareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다 : " + id));

        //조회수 증가
        Integer view = share.getViews() + 1;
        share.setViews(view);
        shareRepository.save(share);

        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
        shareDTO.setImageUrls(getImageUrls(share));
        Optional<Address> address = addressRepository.findById(shareDTO.getAddressId());
        shareDTO.setAddress(address.get()
                .getCityName() + " " + address.get().getDistrictName() + " " +
                address.get().getNeighborhoodName());
        boolean isFavorite = shareFavoriteRepository.existsByUserAndParent(loginUser, share);
        shareDTO.setFavorite(isFavorite);
        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
        shareDTO.setFavoriteCount(favoriteCount);

        return shareDTO;
    }

    @Override
    @Transactional
    public void update(ShareDTO shareDTO) throws IOException {
        Share existingShare = shareRepository.findById(shareDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다 : " + shareDTO.getId()));
        shareImageService.deleteByParent(shareDTO.getId());
        shareDTO.setModifiedAt(Instant.now());
        modelMapper.map(shareDTO, existingShare);
        shareRepository.save(existingShare);

        shareImageService.uploadImages(shareDTO.getFiles(), shareDTO.getUserId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Share share = shareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다 : " + id));
        share.setDeletedAt(Instant.now());
        shareRepository.save(share);
    }


    @Override
    public Page<ShareDTO> listAll(Pageable pageable, Boolean isEnd, String sortBy) {
        if (sortBy.equals("locationDate")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("locationDate")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }


        if(isEnd){
            return shareRepository.findByIsEndIsFalse(pageable)
                    .map(share -> {
                        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                        shareDTO.setImageUrls(getImageThumbnailUrls(share));
                        Optional<Address> address = addressRepository.findById(shareDTO.getAddressId());
                        shareDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
                        shareDTO.setFavoriteCount(favoriteCount);
                        return shareDTO;
                    });
        } else{
            return shareRepository.findAll(pageable)
                    .map(share -> {
                        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                        shareDTO.setImageUrls(getImageThumbnailUrls(share));
                        Optional<Address> address = addressRepository.findById(shareDTO.getAddressId());
                        shareDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
                        shareDTO.setFavoriteCount(favoriteCount);
                        return shareDTO;
                    });
        }
    }

    @Override
    public Page<ShareDTO> searchByKeywords(String keyword, Pageable pageable, Boolean isEnd, String sortBy) {
        if (sortBy.equals("locationDate")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("locationDate")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }

        if(isEnd){
            return shareRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                    .map(share -> {
                        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                        shareDTO.setImageUrls(getImageThumbnailUrls(share));
                        Optional<Address> address = addressRepository.findById(shareDTO.getAddressId());
                        shareDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
                        shareDTO.setFavoriteCount(favoriteCount);
                        return shareDTO;
                    });
        }else{
            return shareRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
                    .map(share -> {
                        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                        shareDTO.setImageUrls(getImageThumbnailUrls(share));
                        Optional<Address> address = addressRepository.findById(shareDTO.getAddressId());
                        shareDTO.setAddress(address.get()
                                .getCityName() + " " + address.get().getDistrictName() + " " +
                                address.get().getNeighborhoodName());
                        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
                        shareDTO.setFavoriteCount(favoriteCount);
                        return shareDTO;
                    });
        }

    }

    @Override
    @Transactional
    public void updateIsEnd() {
        List<Share> shares = shareRepository.findByIsEndIsFalse();
        Instant now = Instant.now();
        shares.forEach(share -> {
            if(share.getLocationDate().isBefore(now)){
                share.setIsEnd(true);
                shareRepository.save(share);
            }
        });
    }

    /*
    // 끝나지 않은 게시글 조회 // 따로 두지 않고 listAll에 isEnd=false 조건 추가
    @Override
    public Page<ShareDTO> listNotEnd(Pageable pageable) {
        return shareRepository.findByIsEndIsFalse(pageable)
                .map(share -> {
                    ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
                    shareDTO.setImageUrls(getImageUrls(share));

                    Integer favoriteCount = shareFavoriteRepository.countByParent(share);
                    shareDTO.setFavoriteCount(favoriteCount);
                    return shareDTO;
                });
    }
     */

    private List<String> getImageUrls(Share share) {
        List<String> imageUrls = shareImageRepository.findByParentAndDeletedAtIsNull(share)
                .stream()
                .map(ShareImage::getServerName)
                .collect(Collectors.toList());
        if (imageUrls.isEmpty()) {
            imageUrls.add(defaultImageUrl);
        }
        return imageUrls;
    }

    private List<String> getImageThumbnailUrls(Share share) {
        List<String> imageUrls = shareImageRepository.findByParentAndDeletedAtIsNull(share)
                .stream()
                .map(shareImage -> "t_" + shareImage.getServerName()) // 썸네일 이미지 이름 생성
                .findFirst()
                .map(List::of)
                .orElseGet(() -> List.of(defaultImageUrl));
        return imageUrls;
    }


}