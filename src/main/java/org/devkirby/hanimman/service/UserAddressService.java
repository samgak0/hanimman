package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.UserAddress;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.devkirby.hanimman.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    private static final Logger logger = LoggerFactory.getLogger(UserAddressService.class);

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    // 주소 저장
    public UserAddressDTO saveUserAddress(UserAddressDTO userAddressDTO) {
        // 유효성 검사
        if (userAddressDTO.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (userAddressDTO.getPrimaryAddressId() == null) {
            throw new IllegalArgumentException("기본 주소 ID는 필수입니다.");
        }

        logger.info("주소 저장 요청: {}", userAddressDTO);
        UserAddress userAddress = new UserAddress();

        // User 객체 설정
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다"));
        userAddress.setUser(user);

        // Primary Address 객체 설정
        Address primaryAddress = addressRepository.findById(userAddressDTO.getPrimaryAddressId())
                .orElseThrow(() -> new RuntimeException("첫번째 주소를 찾을 수 없습니다"));
        userAddress.setPrimaryAddress(primaryAddress);

        // Secondary Address 객체 설정 (null 체크)
        if (userAddressDTO.getSecondlyAddressId() != null) {
            Address secondlyAddress = addressRepository.findById(userAddressDTO.getSecondlyAddressId())
                    .orElseThrow(() -> new RuntimeException("두번째 주소를 찾을 수 없습니다"));
            userAddress.setSecondlyAddress(secondlyAddress);
        }

        // 기타 필드 설정
        userAddress.setValidatedAt(userAddressDTO.getValidatedAt());
        userAddress.setModifiedAt(userAddressDTO.getModifiedAt());
        userAddress.setCreatedAt(userAddressDTO.getCreatedAt());

        // 엔티티 저장
        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return convertToDTO(savedAddress);
    }

    // 주소 조회
    public Optional<UserAddressDTO> getUserAddress(Integer id) {
        logger.info("주소 조회 요청: ID = {}", id);
        return userAddressRepository.findById(id)
                .map(this::convertToDTO);
    }

    // 모든 주소 조회
    public List<UserAddressDTO> getAllUserAddresses(long userId) {
        logger.info("모든 주소 조회 요청: 사용자 ID = {}", userId);
        return userAddressRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주소 삭제
    public void deleteUserAddress(Integer id) {
        logger.info("주소 삭제 요청: ID = {}", id);
        userAddressRepository.deleteById(id);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private UserAddressDTO convertToDTO(UserAddress userAddress) {
        UserAddressDTO.UserAddressDTOBuilder builder = UserAddressDTO.builder()
                .id(userAddress.getId())
                .userId(userAddress.getUser().getId())
                .primaryAddressId(userAddress.getPrimaryAddress().getNeighborhoodName())
                .validatedAt(userAddress.getValidatedAt())
                .modifiedAt(userAddress.getModifiedAt())
                .createdAt(userAddress.getCreatedAt());

        // SecondlyAddress null 체크
        if (userAddress.getSecondlyAddress() != null) {
            builder.secondlyAddressId(userAddress.getSecondlyAddress().getNeighborhoodCode());
        } else {
            builder.secondlyAddressId(null); // 또는 기본값 설정
        }

        return builder.build();
    }
}
