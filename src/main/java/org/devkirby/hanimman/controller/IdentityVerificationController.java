package org.devkirby.hanimman.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/identity-verifications")
public class IdentityVerificationController {
    @Value("${portone.api.secret}")
    private String portOneApiSecret; // 포트원 API 비밀키
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 본인인증 컨트롤러
     * 
     * @param restTemplate RestTemplate
     * @param objectMapper ObjectMapper
     */
    public IdentityVerificationController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 본인인증 POST 요청
     * 
     * @param request 본인인증 요청 정보
     * @param session 세션
     * @return 본인인증 결과
     */
    @PostMapping
    public ResponseEntity<?> verifyIdentityPost(@RequestBody VerificationRequest request, HttpSession session) {
        return verifyIdentity(request.getIdentityVerificationId(), session);
    }

    /**
     * 본인인증 GET 요청
     * 
     * @param identityVerificationId 본인인증 아이디
     * @param session                세션
     * @return 본인인증 결과
     */
    @GetMapping
    public ResponseEntity<?> verifyIdentityGet(@RequestParam String identityVerificationId, HttpSession session) {
        // verifyIdentity 메서드 호출 (이 부분에서 실제 데이터 처리)
        ResponseEntity<?> response = verifyIdentity(identityVerificationId, session);

        // verifyIdentity에서 얻은 값 (예: 개인정보)
        String identityVerificationResult = response.getBody() != null ? response.getBody().toString() : ""; // null 체크
                                                                                                             // 추가

        // React 앱의 API 엔드포인트 --> 테스트 하실 때 본인 노트북 아이피 주소 입력
        String url = "https://hanimman.samgak.store/verification/mobile"; // 모바일

        // HTTP 요청 헤더 준비
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 전송할 데이터 준비 (JSON 형식)
        String jsonBody = "{\"identityVerificationResult\": \"" + identityVerificationResult + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            // RestTemplate을 사용하여 POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 반환
            return ResponseEntity.ok(responseEntity.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 예외 처리: 서버 또는 클라이언트 오류가 발생했을 때
            return ResponseEntity.status(e.getStatusCode()).body("Error occurred: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    /**
     * 본인인증 조회
     * 
     * @param identityVerificationId 본인인증 아이디
     * @param session                세션
     * @return 본인인증 결과
     */
    private ResponseEntity<?> verifyIdentity(String identityVerificationId, HttpSession session) {
        try {
            String url = "https://api.portone.io/identity-verifications/" + identityVerificationId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "PortOne " + portOneApiSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 조회 실패");
            }
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String status = jsonResponse.path("status").asText();
            if (!"VERIFIED".equals(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("본인인증 실패");
            }
            JsonNode verifiedCustomer = jsonResponse.get("verifiedCustomer");
            session.setAttribute("verifiedCustomer", verifiedCustomer);
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