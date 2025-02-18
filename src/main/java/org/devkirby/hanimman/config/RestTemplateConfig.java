package org.devkirby.hanimman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
