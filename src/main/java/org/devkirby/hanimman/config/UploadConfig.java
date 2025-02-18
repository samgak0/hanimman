package org.devkirby.hanimman.config;

import org.devkirby.hanimman.util.ImageUploadUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {
    @Bean
    private ImageUploadUtil imageUploadUtil() {
        return new ImageUploadUtil();
    }
}
