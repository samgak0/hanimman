package org.devkirby.hanimman.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@CrossOrigin
@RestController
@RequestMapping("/identity-verifications")
public class IdentityVerificationController {
    @Value("${portone.api.secret}")
    private String portOneApiSecret; // 포트원 API 비밀키
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IdentityVerificationController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> verifyIdentityPost(@RequestBody VerificationRequest request, HttpSession session) {
        return verifyIdentity(request.getIdentityVerificationId(), session);
    }


    @GetMapping
    public ResponseEntity<?> verifyIdentityGet(@RequestParam String identityVerificationId, HttpSession session) {
        // verifyIdentity 메서드 호출 (이 부분에서 실제 데이터 처리)
        ResponseEntity response = verifyIdentity(identityVerificationId, session);

        // verifyIdentity에서 얻은 값 (예: 개인정보)
        String identityVerificationResult = response.getBody().toString(); // 실제 값을 추출

        // React 앱의 API 엔드포인트 --> 테스트 하실 때 본인 노트북 아이피 주소 입력
        String url = "http://192.168.101.253:3000/verification/mobile"; // 모바일

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
            System.out.println("Error response: " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body("Error occurred: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    private ResponseEntity<?> verifyIdentity(String identityVerificationId, HttpSession session) {
        try {
            String url = "https://api.portone.io/identity-verifications/" + identityVerificationId;
            System.out.println(url);
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
            System.out.println(verifiedCustomer);
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