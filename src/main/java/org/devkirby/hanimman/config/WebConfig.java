package org.devkirby.hanimman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정을 적용
                .allowedOrigins("http://localhost:3000", "http://192.168.101.253:3000", "http://192.168.100.207:3000",
                        "https://hanimman.netlify.app", "https://hanimman.samgak.store", "https://www.samgak.store",
                        "https://samgak.store")// React 앱의 주소
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Authorization") // 클라이언트가 접근할 수 있도록 헤더 노출
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowCredentials(true); // 자격 증명 허용
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/upload/");
    }
}