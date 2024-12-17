package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.UserAddressDTO;
import org.devkirby.hanimman.entity.UserAddress;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.entity.Address;
import org.devkirby.hanimman.repository.UserAddressRepository;
import org.devkirby.hanimman.repository.UserRepository; // UserRepository 추가
import org.devkirby.hanimman.repository.AddressRepository; // AddressRepository 추가
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    // 주소 저장
    public UserAddressDTO saveUserAddress(UserAddressDTO userAddressDTO) {

        if (userAddressDTO.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (userAddressDTO.getPrimaryAddressId() == null) {
            throw new IllegalArgumentException("기본 주소 ID는 필수입니다.");
        }

        // 이미 주소가 등록되어 있는지 확인
        UserAddress existingAddress = userAddressRepository.findByUserId(userAddressDTO.getUserId());
        if (existingAddress != null) {
            return convertToDTO(existingAddress);
        }

        System.out.println("주소 저장 요청:" + userAddressDTO);
        UserAddress userAddress = new UserAddress();

        // User 객체 설정
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다"));
        userAddress.setUser(user);
        System.out.println("유저정보" + user);

        // Primary Address 객체 설정
        Address primaryAddress = addressRepository.findById(userAddressDTO.getPrimaryAddressId())
                .orElseThrow(() -> new RuntimeException("첫번째 주소를 찾을 수 없습니다"));
        userAddress.setPrimaryAddress(primaryAddress);

        // Secondary Address 객체 설정 (null 체크)
        if (userAddressDTO.getSecondlyAddressId() != null) {
            Address secondlyAddress = addressRepository.findById(userAddressDTO.getSecondlyAddressId())
                    .orElseThrow(() -> new RuntimeException("두번째 주소를 찾을 수 없습니다"));
            userAddress.setSecondlyAddress(secondlyAddress);
        } else {
            userAddress.setSecondlyAddress(null);
        }

        // 기타 필드 설정
        userAddress.setValidatedAt(userAddressDTO.getValidatedAt());
        userAddress.setModifiedAt(userAddressDTO.getModifiedAt());
        userAddress.setCreatedAt(userAddressDTO.getCreatedAt());

        // 엔티티 저장
        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return convertToDTO(savedAddress);
    }

    // 단일 주소 조회 메서드
    public Optional<UserAddressDTO> getUserAddress(Integer userId) {
        UserAddress userAddress = userAddressRepository.findByUserId(userId);
        // UserAddress가 null인 경우 Optional.empty() 반환
        return Optional.ofNullable(userAddress)
                .map(address -> modelMapper.map(address, UserAddressDTO.class));
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
        UserAddressDTO.UserAddressDTOBuilder builder = UserAddressDTO.builder()
                .id(userAddress.getId())
                .userId(userAddress.getUser().getId())
                .primaryAddressId(userAddress.getPrimaryAddress().getId())
                .validatedAt(userAddress.getValidatedAt())
                .modifiedAt(userAddress.getModifiedAt())
                .createdAt(userAddress.getCreatedAt());

        // SecondlyAddress null 체크
        if (userAddress.getSecondlyAddress() != null) {
            builder.secondlyAddressId(userAddress.getSecondlyAddress().getId());
        } else {
            builder.secondlyAddressId(null);
        }
        return builder.build();
    }

    //두번째 주소 저장
    public UserAddressDTO saveSecondaryUserAddress(UserAddressDTO userAddressDTO) {

        if (userAddressDTO.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (userAddressDTO.getPrimaryAddressId() == null) {
            throw new IllegalArgumentException("기본 주소 ID는 필수입니다.");
        }

        // 기존 주소 조회
        UserAddress existingAddress = userAddressRepository.findByUserId(userAddressDTO.getUserId());

        if (existingAddress != null) {
            // 주소가 이미 존재하면 업데이트 로직으로 전환

            Address secondlyAddress = addressRepository.findById(userAddressDTO.getPrimaryAddressId())
                    .orElseThrow(() -> new RuntimeException("두번째 주소를 찾을 수 없습니다."));
            System.out.println(secondlyAddress);
            existingAddress.setSecondlyAddress(secondlyAddress); // Address 객체로 설정
            // 날짜 필드 업데이트
            existingAddress.setValidatedAt(userAddressDTO.getValidatedAt());
            existingAddress.setModifiedAt(Instant.now()); // 수정 시간 업데이트

            // 엔티티 저장
            UserAddress updatedAddress = userAddressRepository.save(existingAddress);
            System.out.println(updatedAddress);
            return convertToDTO(updatedAddress);
        }

        // 새로운 주소를 생성하는 경우
        UserAddress userAddress = new UserAddress();
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
        userAddress.setUser(user);

        // Primary Address 객체 설정
        Address primaryAddress = addressRepository.findById(userAddressDTO.getPrimaryAddressId())
                .orElseThrow(() -> new RuntimeException("첫번째 주소를 찾을 수 없습니다."));
        userAddress.setPrimaryAddress(primaryAddress);

        // Secondary Address 설정
        if (userAddressDTO.getSecondlyAddressId() != null) {
            Address secondlyAddress = addressRepository.findById(userAddressDTO.getSecondlyAddressId())
                    .orElseThrow(() -> new RuntimeException("두번째 주소를 찾을 수 없습니다."));
            userAddress.setSecondlyAddress(secondlyAddress);
        } else {
            userAddress.setSecondlyAddress(null); // 명시적으로 null 설정
        }

        // 기타 필드 설정
        userAddress.setValidatedAt(userAddressDTO.getValidatedAt());
        userAddress.setCreatedAt(Instant.now());
        userAddress.setModifiedAt(Instant.now());

        // 엔티티 저장
        UserAddress savedAddress = userAddressRepository.save(userAddress);
        return convertToDTO(savedAddress);
    }



    // 주소 업데이트 메서드
    public UserAddressDTO updateUserAddress(UserAddressDTO userAddressDTO) {
        // 주소 엔티티 조회
        UserAddress userAddress = userAddressRepository.findById(userAddressDTO.getId())
                .orElseThrow(() -> new RuntimeException("주소 정보를 찾을 수 없습니다."));
        System.out.println("주소 업데이트 요청:" + userAddressDTO);

        // User 객체 설정
        User user = userRepository.findById(userAddressDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        System.out.println("유저 정보:" + user);

        // Primary Address 객체 설정
        Address primaryAddress = addressRepository.findById(userAddressDTO.getPrimaryAddressId())
                .orElseThrow(() -> new RuntimeException("첫번째 주소를 찾을 수 없습니다."));
        System.out.println("첫번째 주소:" + primaryAddress);

        // Secondary Address 객체 설정 (null 체크)
        Address secondlyAddress = null;
        if (userAddressDTO.getSecondlyAddressId() != null) {
            secondlyAddress = addressRepository.findById(userAddressDTO.getSecondlyAddressId())
                    .orElseThrow(() -> new RuntimeException("두번째 주소를 찾을 수 없습니다."));
        }

        // 첫 번째 주소가 비어있지 않으면 기존 주소 유지
        userAddress.setPrimaryAddress(primaryAddress); // 항상 primaryAddress를 업데이트

        // 두 번째 주소 설정
        userAddress.setSecondlyAddress(secondlyAddress);

        // 날짜 필드 설정
        userAddress.setValidatedAt(userAddressDTO.getValidatedAt() != null ? userAddressDTO.getValidatedAt() : Instant.now());
        userAddress.setModifiedAt(Instant.now()); // 수정 시간은 현재 시간으로 설정

        // 엔티티 저장
        UserAddress updatedAddress = userAddressRepository.save(userAddress);

        // DTO로 변환하여 반환
        return convertToDTO(updatedAddress);
    }

    // 주소 조회
    public String selectUserAddressName(String userAddressId){
        Optional<Address> opt = addressRepository.findById(userAddressId);
        Address address = opt.orElseThrow();
        String addressName = address.getNeighborhoodName();
        return addressName;
    }
}