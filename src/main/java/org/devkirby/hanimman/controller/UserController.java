package org.devkirby.hanimman.controller;

import lombok.extern.log4j.Log4j2;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Gender;
import org.devkirby.hanimman.entity.Nickname;
import org.devkirby.hanimman.service.UserService;
import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log4j2
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAndSignupOrLogin(@RequestBody ResultRequest resultData) {

        // 필수 필드 검증
        if (resultData.getName() == null || resultData.getPhoneNumber() == null ||
                resultData.getBirthDate() == null || resultData.getGender() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Missing required fields."));
        }

        try {
            // ResultRequest -> UserDTO 변환
            UserDTO userDTO = JsonToDTO(resultData);

            // 유저가 존재하는지 확인
            UserDTO existingUser = userService.selectUser(userDTO);
            if (existingUser == null || existingUser.getId() == null) {
                // 유저가 없으면 회원가입 진행
                String nickname = nicknameGenerator();
                String codeNum = codenumGenerator();
                // 유저의 코드번호가 중복이 안 될 때까지 생성
                boolean isExistCodeNum = true;
                while (isExistCodeNum){
                    isExistCodeNum = userService.isExistCodeNum(codeNum);
                }
                userDTO.setCodenum(codeNum);
                userDTO.setNickname(nickname);

                userService.createUser(userDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new SuccessResponse("Sign-up successful."));
            }

            // 로그인 성공 -> JWT 생성
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "user");  // 추가적인 클레임 (예: role, 권한 등)
            claims.put("username", existingUser.getName());
            claims.put("phoneNumber", existingUser.getPhonenum());

            // JWT 생성
            String token = JWTUtil.generateToken(claims, existingUser.getId());

            // JWT를 응답으로 반환
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(new SuccessResponse("Login successful."));
        } catch (Exception e) {
            // 예외 처리 및 로깅
            log.error("Error during verification, signup, or login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred during the process."));
        }
    }

    // Converts ResultRequest to UserDTO
    private UserDTO JsonToDTO(ResultRequest resultData) {
        Gender gender = resultData.getGender().equalsIgnoreCase("MALE") ? Gender.M : Gender.F;
        return UserDTO.builder()
                .name(resultData.getName())
                .phonenum(resultData.getPhoneNumber())
                .gender(gender)
                .birth(LocalDate.parse(resultData.getBirthDate()))
                .build();
    }

    public String nicknameGenerator() {
        Random random = new Random();
        Nickname[] nicknames = Nickname.values();
        Nickname selectNickname = nicknames[random.nextInt(nicknames.length)];
        return selectNickname.toString().replace("_", " "); // Ensure the replacement is applied
    }

    public static String codenumGenerator() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }

        return code.toString();
    }

    // Error response DTO
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getter
        public String getMessage() {
            return message;
        }
    }

    // Success response DTO
    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        // Getter
        public String getMessage() {
            return message;
        }
    }

    public static class ResultRequest {
        private String name;
        private String phoneNumber;
        private String birthDate;
        private String gender;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getBirthDate() { return birthDate; }
        public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
    }
}
