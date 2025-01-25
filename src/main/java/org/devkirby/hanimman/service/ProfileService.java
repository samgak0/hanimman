package org.devkirby.hanimman.service;

import lombok.Setter;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ProfileRepository;
import org.devkirby.hanimman.repository.UserRepository;
import org.devkirby.hanimman.util.ImageUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProfileRepository profileRepository; // 프로필 저장소

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadUtil imageUploadUtil;

    // Setter 메서드 추가
    @Value("${com.devkirby.hanimman.upload}")
    @Setter
    private String uploadDir; // 파일 저장 디렉토리

    public Profile uploadProfilePicture(MultipartFile file, Profile profile) throws IOException {
        // 파일 이름 가져오기
        String originalFileName = file.getOriginalFilename();

        // UUID 생성
        String uuid = UUID.randomUUID().toString(); // UUID 생성

        // serverName에 파일 이름과 UUID 결합
        String fileNameWithUUID = uuid + '-' + originalFileName;
        profile.setServerName(fileNameWithUUID);

        // 파일 경로 설정
        Path filePath = Paths.get(uploadDir + "/" + fileNameWithUUID);

        // 파일 저장
        file.transferTo(filePath.toFile());

        // 프로필 정보 업데이트
        profile.setFileSize((int) file.getSize());
        profile.setMineType(file.getContentType());
        profile.setRealName(originalFileName); // 원래 파일 이름

        // 프로필 저장
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile selectByUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Profile profile = profileRepository.findByParent(user);
        return profile;
    }

    public void createProfile(UserDTO savedUserDTO) {
        User user = modelMapper.map(savedUserDTO, User.class);
        // User가 DB에 저장된 상태인지 확인

        User persistentUser = userRepository.findById(user.getId())
                .orElseGet(() -> userRepository.save(user)); // 없으면 저장

        Profile profile = Profile.builder()
                .realName("default-profile")
                .serverName("default-profile")
                .mineType("png")
                .fileSize(1)
                .parent(persistentUser)
                .createdAt(Instant.now())
                .build();
        profileRepository.save(profile);
    }

    public void updateProfile(UserDTO userDTO, MultipartFile profileImage) throws IOException {
        User user = modelMapper.map(userDTO, User.class);
        String mineType = profileImage.getContentType();
        try {
            String serverName = imageUploadUtil.uploadImage(profileImage);
            Profile profile = selectByUser(userDTO);
            if (profile == null) {
                profile = new Profile();
                profile.setParent(user); // parent 필드 설정
            }
            profile.setRealName(profileImage.getOriginalFilename());
            profile.setServerName(serverName);
            profile.setFileSize((int) profileImage.getSize());
            profile.setMineType(mineType);
            profileRepository.save(profile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getProfile(UserDTO userDTO) {
        File file = null; // 파일 객체를 초기화
        User user = modelMapper.map(userDTO, User.class);
        Profile profile = profileRepository.findByParent(user);
        String serverName = profile.getServerName();

        if (serverName != null && !serverName.isEmpty()) {
            Path filePath = Paths.get("C:/upload", serverName);
            file = new File(filePath.toString());
            if (file.exists()) {
            } else {
                System.out.println("File does not exist.");
            }
        } else {
            System.out.println("Server name is invalid.");
        }

        return file;
    }

    public String getProfileImageUrl(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Profile profile = profileRepository.findByParent(user);
        if (profile == null) {
            return null;
        }
        String serverName = "t_" + profile.getServerName();
        return serverName;
    }

    public Integer getProfileImageUrlId(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Profile profile = profileRepository.findByParent(user);
        if (profile == null) {
            return null;
        }
        return profile.getId();
    }
}