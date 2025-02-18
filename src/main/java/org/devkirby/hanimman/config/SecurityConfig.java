package org.devkirby.hanimman.config;

import org.devkirby.hanimman.filter.JWTAuthenticationFilter;
import org.devkirby.hanimman.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
    }

    @Bean
    private JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(userService);
    }

    @Bean
    private SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/identity-verifications/**").permitAll() // 특정 API 경로 허용
                        .requestMatchers("/*/**").permitAll()
                        .anyRequest().authenticated() // 나머지 모든 요청에 대해 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }
}
