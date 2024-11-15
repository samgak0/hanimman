package org.devkirby.hanimman.config;

import org.devkirby.hanimman.util.ImageUploadUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {
    @Bean
    public ImageUploadUtil imageUploadUtil() {
        return new ImageUploadUtil();
    }
}
