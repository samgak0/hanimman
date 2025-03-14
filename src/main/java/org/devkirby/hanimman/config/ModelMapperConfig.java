package org.devkirby.hanimman.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    private ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
