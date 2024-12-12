package org.devkirby.hanimman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 자격 증명 허용 (쿠키, 인증 헤더 등)
        config.addAllowedOriginPattern("*"); // 모든 출처 허용 (필요에 따라 변경 가능)
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST, PUT 등)
        config.addExposedHeader("Authorization"); // 클라이언트가 읽을 수 있는 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 설정 적용
        return new CorsFilter(source);
    }
}
