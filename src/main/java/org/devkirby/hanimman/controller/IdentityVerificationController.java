package org.devkirby.hanimman.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.devkirby.hanimman.dto.UserDTO;
import org.devkirby.hanimman.entity.Gender;
import org.devkirby.hanimman.entity.Nickname;
import org.devkirby.hanimman.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Random;

/*
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/identity-verifications")
public class IdentityVerificationController {

    @Value("${portone.api.secret}")
    private String portOneApiSecret;  // 포트원 API 비밀키
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // RestTemplate과 ObjectMapper를 생성자에서 주입받기
    public IdentityVerificationController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    @PostMapping
    public ResponseEntity<?> verifyIdentity(@RequestBody VerificationRequest request, HttpSession session) {
        String identityVerificationId = request.getIdentityVerificationId();
        try {
            // 포트원 API를 통해 인증 결과 조회
            String url = "https://api.portone.io/identity-verifications/" + identityVerificationId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "PortOne " + portOneApiSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 포트원 API 호출
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            HttpStatusCode statusCode = response.getStatusCode();

            if (statusCode != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 조회 실패");
            }

            // JSON 응답 파싱
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String status = jsonResponse.path("status").asText();

            if (!"VERIFIED".equals(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("본인인증 실패");
            }

            // 인증 성공 후 필요한 정보 처리
            JsonNode verifiedCustomer = jsonResponse.get("verifiedCustomer");
            // 본인 인증 정보를 세션에 저장
            session.setAttribute("verifiedCustomer", verifiedCustomer);
            // 인증 성공 응답
            return ResponseEntity.ok(verifiedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }

    public static class VerificationRequest {
        private String identityVerificationId;

        public String getIdentityVerificationId() {
            return identityVerificationId;
        }

        public void setIdentityVerificationId(String identityVerificationId) {
            this.identityVerificationId = identityVerificationId;
        }
    }
}
