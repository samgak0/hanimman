package org.devkirby.hanimman.controller;

import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh-token")
public class TokenController {

    @PostMapping
    public ResponseEntity<?> refreshAuthToken(@RequestBody TokenRequest tokenRequest) {
        try {
            String refreshToken = tokenRequest.getToken();
            // 리프레시 토큰의 유효성 검사 및 새로운 JWT 토큰 발급
            if (JWTUtil.validateToken(refreshToken) != null) {
                String newToken = JWTUtil.generateToken(JWTUtil.validateToken(refreshToken), "someAddKey");
                return ResponseEntity.ok(new TokenResponse(newToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token refresh failed");
        }
    }

    public static class TokenRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
