package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.config.CustomUserDetails;
import org.devkirby.hanimman.dto.TogetherDTO;
import org.devkirby.hanimman.dto.TogetherLocationDTO;
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
public class TogetherServiceImpl implements TogetherService {
    private final TogetherRepository togetherRepository;
    private final TogetherImageRepository togetherImageRepository;
    private final TogetherFavoriteRepository togetherFavoriteRepository;
    private final TogetherImageService togetherImageService;
    private final ProfileRepository profileRepository;
    private final TogetherParticipantRepository togetherParticipantRepository;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final MarketRepository marketRepository;
    private final TogetherLocationRepository togetherLocationRepository;
    private final ModelMapper modelMapper;
    private final UserAddressRepository userAddressRepository;

    private final Logger log = LoggerFactory.getLogger(TogetherServiceImpl.class);
    @Value("${com.devkirby.hanimman.upload.default_image.png}")
    private String defaultImageUrl;

    @Override
    @Transactional
    public Integer create(TogetherDTO togetherDTO, String primaryAddressId) throws IOException {
        Optional<User> user = userRepository.findById(togetherDTO.getUserId());
        Market market = marketRepository.findByCategoryIdAndName(togetherDTO.getMarketCategory(),
                togetherDTO.getMarketName());
        String marketCategoryName;
        if(togetherDTO.getMarketCategory() == null){
            Optional<Address> address = addressRepository.findById(togetherDTO.getAddressDTO().getId());
            togetherDTO.setAddressId(address.get().getId());
        }else{
            togetherDTO.setAddressId(market.getAddress().getId());
        }
        Together together = modelMapper.map(togetherDTO, Together.class);
        togetherDTO.setId(togetherRepository.save(together).getId());


        if(togetherDTO.getMarketCategory() == null){
            Optional<Address> address = addressRepository.findById(togetherDTO.getAddressDTO().getId());
            TogetherLocation togetherLocation = TogetherLocation.builder()
                    .together(togetherRepository.findById(togetherDTO.getId()).get())
                    .latitude(togetherDTO.getLatitude())
                    .longitude(togetherDTO.getLongitude())
                    .address(address.get())
                    .detail(togetherDTO.getAddress())
                    .build();
            togetherLocationRepository.save(togetherLocation);
        }else if(togetherDTO.getMarketCategory() == 1){
            marketCategoryName = "코스트코 ";
            TogetherLocation togetherLocation = TogetherLocation.builder()
                    .together(togetherRepository.findById(togetherDTO.getId()).get())
                    .latitude(market.getLatitude())
                    .longitude(market.getLongitude())
                    .address(market.getAddress())
                    .detail(marketCategoryName+market.getName())
                    .build();
            togetherLocationRepository.save(togetherLocation);
        }else if(togetherDTO.getMarketCategory() == 2) {
            marketCategoryName = "트레이더스 ";
            TogetherLocation togetherLocation = TogetherLocation.builder()
                    .together(togetherRepository.findById(togetherDTO.getId()).get())
                    .latitude(market.getLatitude())
                    .longitude(market.getLongitude())
                    .address(market.getAddress())
                    .detail(marketCategoryName + market.getName())
                    .build();
            togetherLocationRepository.save(togetherLocation);
        }

        if(togetherDTO.getFiles() != null && !togetherDTO.getFiles().isEmpty()){
            togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getId());
        }
        return togetherDTO.getId();
    }

    @Override
    public TogetherDTO read(Integer id, CustomUserDetails loginUser) {
        Together together = togetherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 같이가요 게시글이 없습니다. : " + id));

        //조회수 증가
        Integer view = together.getViews() + 1;
        together.setViews(view);
        togetherRepository.save(together);
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다. : " + loginUser.getId()));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        Optional<User> parent = userRepository.findById(together.getUser().getId());
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

        User user2 = userRepository.findById(together.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다. : " + together.getUser().getId()));
        UserDTO userDTO2 = modelMapper.map(user2, UserDTO.class);
        togetherDTO.setImageIds(getImageUrls(together));
        togetherDTO.setUserNickname(together.getUser().getNickname());
        togetherDTO.setUserProfileImage(profileService.getProfileImageUrlId(userDTO2));
        togetherDTO.setBrix(parent.get().getBrix());
        TogetherLocation togetherLocation = togetherLocationRepository.findByTogetherId(together.getId());
        if(togetherLocation == null){
            togetherLocation = TogetherLocation.builder()
                    .latitude("0")
                    .longitude("0")
                    .detail("위치 정보 없음")
                    .address(addressRepository.findById("1111010700").get())
                    .build();
        }
        togetherDTO.setTogetherLocationDTO(modelMapper.map(togetherLocation, TogetherLocationDTO.class));
        String cityName = togetherLocation.getAddress().getCityName() + " " +
                togetherLocation.getAddress().getDistrictName() + " " +
                togetherLocation.getAddress().getNeighborhoodName();
        togetherDTO.setAddress(cityName);
        boolean isFavorite = togetherFavoriteRepository.existsByUserAndParent(user, together);
        togetherDTO.setFavorite(isFavorite);
        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
        togetherDTO.setFavoriteCount(favoriteCount);
        togetherDTO.setParticipantCount(togetherParticipantRepository.countByParentId(together.getId()));

        return togetherDTO;
    }

    @Override
    @Transactional
    public void update(TogetherDTO togetherDTO) throws IOException {
        Together existingTogether = togetherRepository.findById(togetherDTO.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 같이가요 게시글이 없습니다. : " + togetherDTO.getId()));
        togetherImageService.deleteByParent(togetherDTO.getId());
        existingTogether = modelMapper.map(togetherDTO, Together.class);
        existingTogether.setModifiedAt(Instant.now());
        togetherRepository.save(existingTogether);

        if(togetherDTO.getFiles() != null && !togetherDTO.getFiles().isEmpty()){
            togetherImageService.uploadImages(togetherDTO.getFiles(), togetherDTO.getId());
        }
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
    public Page<TogetherDTO> listAll
            (Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 주소가 없습니다. : " + addressId));
        String userCityCode = address.getCityCode();
        if (sortBy.equals("meetingAt")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.asc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(!isEnd){
            return togetherRepository.
                    findByAddress_CityCodeAndIsEndIsFalseAndDeletedAtIsNull
                            (pageable, userCityCode)
                    .map(this::getTogetherDTO);
        }
        else{
            return togetherRepository.
                    findByAddress_CityCodeAndDeletedAtIsNull
                            (pageable, userCityCode)
                    .map(this::getTogetherDTO);
        }
    }

    @Override
    public Page<TogetherDTO> searchByKeywords
            (String keyword, Pageable pageable, Boolean isEnd, String sortBy, String addressId, Integer userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 주소가 없습니다. : " + addressId));
        String userCityCode = address.getCityCode();

        if (sortBy.equals("meetingAt")) {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.asc("meetingAt")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        }
        if(!isEnd){
            return togetherRepository.findByAddress_CityCodeAndTitleContainingOrContentContainingAndIsEndIsFalseAndDeletedAtIsNull(
                            pageable, userCityCode, keyword, keyword)
                    .map(this::getTogetherDTO);
        }else{
            return togetherRepository.findByAddress_CityCodeAndTitleContainingOrContentContainingAndDeletedAtIsNull(
                            pageable, userCityCode, keyword, keyword)
                    .map(this::getTogetherDTO);
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
        return togetherPage.map(this::getTogetherDTO);
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
    @Transactional
    public File downloadProfileImage(Integer id) throws IOException {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 프로필 이미지가 없습니다. : " + id));
        File file = new File("C:/upload/" + profile.getServerName());

        return file;
    }

    @Override
    @Transactional
    public void deleteProfileById(Integer id) {
        profileRepository.deleteById(id);
    }

    @Override
    public Page<TogetherDTO> listByUserId(Integer userId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<Together> togetherPage = togetherRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        return togetherPage.map(this::getTogetherDTO);
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

    private TogetherDTO getTogetherDTO(Together together) {
        TogetherDTO togetherDTO = modelMapper.map(together, TogetherDTO.class);
        togetherDTO.setImageIds(getImageThumbnailUrls(together));
        TogetherLocation togetherLocation = togetherLocationRepository.findByTogetherId(together.getId());
        if(togetherLocation == null){
            togetherLocation = TogetherLocation.builder()
                    .latitude("0")
                    .longitude("0")
                    .detail("위치 정보 없음")
                    .address(addressRepository.findById("1111010700").get())
                    .build();
        }
        if(togetherLocation.getAddress().getNeighborhoodName() != null){
            togetherDTO.setAddress(togetherLocation.getAddress().getNeighborhoodName());
        }else{
            togetherDTO.setAddress(togetherLocation.getAddress().getDistrictName());
        }
        Integer favoriteCount = togetherFavoriteRepository.countByParent(together);
        togetherDTO.setFavoriteCount(favoriteCount);
        togetherDTO.setParticipantCount(togetherParticipantRepository.countByParentId(together.getId()));
        log.info("together Participant : {}", togetherDTO.getParticipantCount());
        return togetherDTO;
    }
}