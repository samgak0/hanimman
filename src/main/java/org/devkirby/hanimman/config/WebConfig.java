package org.devkirby.hanimman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정을 적용
                .allowedOrigins("http://localhost:3000")  // React 앱의 주소
                .allowedHeaders("*")  // 모든 헤더 허용
                .exposedHeaders("Authorization")  // 클라이언트가 접근할 수 있도록 헤더 노출
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowCredentials(true);  // 자격 증명 허용
    }
}
