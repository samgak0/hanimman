package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.ShareDTO;
import org.devkirby.hanimman.dto.ShareLocationDTO;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.*;
import org.devkirby.hanimman.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private static final Logger log = LoggerFactory.getLogger(ShareServiceImpl.class);
    private final ShareRepository shareRepository;
    private final ShareImageRepository shareImageRepository;
    private final ShareFavoriteRepository shareFavoriteRepository;
    private final ShareImageService shareImageService;
    private final ProfileRepository profileRepository;
    private final ShareParticipantRepository shareParticipantRepository;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShareLocationRepository shareLocationRepository;
    private final ModelMapper modelMapper;

    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public Integer create(ShareDTO shareDTO, String primaryAddressId) throws IOException {
        shareDTO.setAddressId(primaryAddressId);
        Share share = modelMapper.map(shareDTO, Share.class);
        shareDTO.setId(shareRepository.save(share).getId());
        Optional<Address> address = addressRepository.findById(shareDTO.getAddressDTO().getId());
        ShareLocation shareLocation = ShareLocation.builder()
                .latitude(shareDTO.getLatitude())
                .longitude(shareDTO.getLongitude())
                .address(address.get())
                .detail(shareDTO.getAddress())
                .share(shareRepository.findById(shareDTO.getId()).get())
                .build();
        shareLocationRepository.save(shareLocation);
        if(shareDTO.getFiles() != null && !shareDTO.getFiles().isEmpty()){
            shareImageService.uploadImages(shareDTO.getFiles(), shareDTO.getId());
        }
        return shareDTO.getId();

    }

    @Override
    public ShareDTO read(Integer id, CustomUserDetails loginUser) {
        Share share = shareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다 : " + id));

        //조회수 증가
        Integer view = share.getViews() + 1;
        share.setViews(view);
        shareRepository.save(share);
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다. : " + loginUser.getId()));
        Optional<User> parent = userRepository.findById(share.getUser().getId());
        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);

        if(Objects.equals(share.getUser().getId(), loginUser.getId())){
            shareDTO.setWriter(true);
        }else {
            shareDTO.setWriter(false);
        }

        if(shareParticipantRepository.existsByUserIdAndParentId(user.getId(), share.getId())) {
            shareDTO.setParticipant(true);
        }else{
            shareDTO.setParticipant(false);
        }

        User user2 = userRepository.findById(share.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다. : " + share.getUser().getId()));
        UserDTO userDTO2 = modelMapper.map(user2, UserDTO.class);
        shareDTO.setImageIds(getImageUrls(share));
        shareDTO.setUserNickname(share.getUser().getNickname());
        shareDTO.setUserProfileImage(profileService.getProfileImageUrlId(userDTO2));
        shareDTO.setBrix(parent.get().getBrix());
        ShareLocation shareLocation = shareLocationRepository.findByShareId(share.getId());
        if(shareLocation == null){
            shareLocation = ShareLocation.builder()
                    .latitude("0")
                    .longitude("0")
                    .detail("위치 정보 없음")
                    .address(addressRepository.findById("1111010700").get())
                    .build();
        }
        shareDTO.setShareLocationDTO(modelMapper.map(shareLocation, ShareLocationDTO.class));
        String cityName=shareLocation.getAddress().getCityName() + " "+
                shareLocation.getAddress().getDistrictName() + " " +
                shareLocation.getAddress().getNeighborhoodName();
        shareDTO.setAddress(cityName);
        boolean isFavorite = shareFavoriteRepository.existsByUserAndParent(user, share);
        shareDTO.setFavorite(isFavorite);
        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
        shareDTO.setFavoriteCount(favoriteCount);
        shareDTO.setParticipantCount(shareParticipantRepository.countByParentId(share.getId()));

        return shareDTO;
    }

    @Override
    @Transactional
    public void update(ShareDTO shareDTO) throws IOException {
        Share existingShare = shareRepository.findById(shareDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 나눠요 게시글이 없습니다 : " + shareDTO.getId()));
        shareImageService.deleteByParent(shareDTO.getId());
        existingShare = modelMapper.map(shareDTO, Share.class);
        existingShare.setModifiedAt(Instant.now());
        shareRepository.save(existingShare);

        if(shareDTO.getFiles() != null && !shareDTO.getFiles().isEmpty()){
            log.info("이미지 업로드 시작");
            shareImageService.uploadImages(shareDTO.getFiles(), shareDTO.getId());
        }

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
    public Page<ShareDTO> listAll
            (Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 주소가 없습니다. : " + addressId));
        String userCityCode = address.getCityCode();
        if (sortBy.equals("locationDate")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("locationDate")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(!isEnd){
            return shareRepository.findByAddress_CityCodeAndIsEndIsFalseAndDeletedAtIsNull
                            (pageable, userCityCode)
                    .map(this::getShareDTO);
        } else{
            return shareRepository.findByAddress_CityCodeAndDeletedAtIsNull
                            (pageable, userCityCode)
                    .map(this::getShareDTO);
        }
    }

    @Override
    public Page<ShareDTO> searchByKeywords
            (String keyword, Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 주소가 없습니다. : " + addressId));
        String userCityCode = address.getCityCode();

        if (sortBy.equals("locationDate")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("locationDate")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }

        if(!isEnd){
            return shareRepository.findByAddress_CityCodeAndTitleContainingOrContentContainingAndIsEndIsFalseAndDeletedAtIsNull
                            (pageable, userCityCode, keyword, keyword)
                    .map(this::getShareDTO);
        }else{
            return shareRepository.findByAddress_CityCodeAndTitleContainingOrContentContainingAndDeletedAtIsNull
                            (pageable, userCityCode, keyword, keyword)
                    .map(this::getShareDTO);
        }

    }

    @Override
    @Transactional
    public void updateIsEnd() {
        List<Share> shares = shareRepository.findByIsEndIsFalse();
        Instant now = Instant.now();
        shares.forEach(share -> {
            Instant locationDate = share.getLocationDate();
            if(locationDate != null && share.getLocationDate().isBefore(now)){
                share.setIsEnd(true);
                shareRepository.save(share);
            }
        });
    }

    @Override
    public Page<ShareDTO> listByUserIdFavorite(Integer userId, Pageable pageable) {
        List<ShareFavorite> shareFavorites = shareFavoriteRepository.findByUserId(userId);
        List<Share> shares = shareFavorites.stream()
                .map(shareFavorite -> shareFavorite.getParent())
                .collect(Collectors.toList());
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));

        Page<Share> sharePage = new PageImpl<>(shares, pageable, shares.size());
        return sharePage.map(this::getShareDTO);
    }

    @Override
    @Transactional
    public File downloadImage(Integer id) throws IOException {
        ShareImage shareImage = shareImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지가 없습니다 : " + id));
        return new File("C:/upload/" + shareImage.getServerName());
    }

    @Override
    @Transactional
    public File downloadProfileImage(Integer id) throws IOException {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 프로필 이미지가 없습니다. : " + id));
        File file = new File("C:/upload/" + profile.getServerName());

        return file;
    }

    @Override
    public Page<ShareDTO> listByUserId(Integer userId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<Share> sharePage = shareRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        return sharePage.map(this::getShareDTO);
    }

    private List<Integer> getImageUrls(Share share) {
        List<Integer> imageUrls = shareImageRepository.findByParentAndDeletedAtIsNull(share)
                .stream()
                .map(shareImage->shareImage.getId())
                .collect(Collectors.toList());

        return imageUrls;
    }

    private List<Integer> getImageThumbnailUrls(Share share) {
        List<Integer> imageUrls = shareImageRepository.findByParentAndDeletedAtIsNull(share)
                .stream()
                .map(shareImage -> shareImage.getId()) // 썸네일 이미지 이름 생성
                .findFirst()
                .map(List::of).orElse(List.of(0));
        return imageUrls;
    }

    private ShareDTO getShareDTO(Share share) {
        ShareDTO shareDTO = modelMapper.map(share, ShareDTO.class);
        shareDTO.setImageIds(getImageThumbnailUrls(share));
        ShareLocation shareLocation = shareLocationRepository.findByShareId(share.getId());
        if(shareLocation == null){
            shareLocation = ShareLocation.builder()
                    .latitude("0")
                    .longitude("0")
                    .detail("위치 정보 없음")
                    .address(addressRepository.findById("1111010700").get())
                    .build();
        }
        if(shareLocation.getAddress().getNeighborhoodName() == null){
            shareDTO.setAddress(shareLocation.getAddress().getDistrictName());
        }else{
            shareDTO.setAddress(shareLocation.getAddress().getNeighborhoodName());
        }
        Integer favoriteCount = shareFavoriteRepository.countByParent(share);
        shareDTO.setFavoriteCount(favoriteCount);
        shareDTO.setParticipantCount(shareParticipantRepository.countByParentId(share.getId()));
        return shareDTO;
    }
}