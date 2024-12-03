package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.UserAddress;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.repository.UserRepository; // UserRepository 추가
import org.devkirby.hanimman.repository.AddressRepository; // AddressRepository 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    @Autowired
    private AddressRepository addressRepository; // AddressRepository 주입

    // 주소 저장
    public UserAddressDTO saveUserAddress(UserAddressDTO userAddressDTO) {
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

        // 엔티티를 DTO로 변환하여 반환
        return convertToDTO(savedAddress);
    }

    // 주소 조회
    public Optional<UserAddressDTO> getUserAddress(Integer id) {
        return userAddressRepository.findById(id)
                .map(this::convertToDTO);
    }

    // 모든 주소 조회
    public List<UserAddressDTO> getAllUserAddresses(long userId) {
        return userAddressRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 주소 삭제
    public void deleteUserAddress(Integer id) {
        userAddressRepository.deleteById(id);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private UserAddressDTO convertToDTO(UserAddress userAddress) {
        return UserAddressDTO.builder()
                .id(userAddress.getId())
                .userId(userAddress.getUser().getId())
                .primaryAddressId(userAddress.getPrimaryAddress().getNeighborhoodName())
                .secondlyAddressId(userAddress.getSecondlyAddress().getNeighborhoodCode())
                .validatedAt(userAddress.getValidatedAt())
                .modifiedAt(userAddress.getModifiedAt())
                .createdAt(userAddress.getCreatedAt())
                .build();
    }
}