package org.devkirby.hanimman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {
    @Bean
    public util.ImageUploadUtil imageUploadUtil() {
        return new util.ImageUploadUtil();
    }
}
