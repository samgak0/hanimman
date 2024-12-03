package org.devkirby.hanimman.service;

import lombok.Setter;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProfileRepository profileRepository; // 프로필 저장소

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
        Path filePath = Paths.get(uploadDir + fileNameWithUUID);
        Path fileDir = Paths.get(uploadDir);

        // 파일 저장
        file.transferTo(filePath.toFile());

        // 프로필 정보 업데이트
        profile.setFileSize((int) file.getSize());
        profile.setMineType(file.getContentType());
        profile.setRealName(originalFileName); // 원래 파일 이름

        // 프로필 저장
        return profileRepository.save(profile);
    }

    public List<Profile> selectByUser(UserDTO userDTO){
        User user  = mapper.map(userDTO, User.class);
       List<Profile> list = profileRepository.findByParent(user);
       return list;
    }
}
