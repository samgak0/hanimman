package org.devkirby.hanimman.config;

import org.devkirby.hanimman.filter.JWTAuthenticationFilter;
import org.devkirby.hanimman.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // JWTAuthenticationFilter를 Bean으로 등록
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) -> csrfConfig.disable())  // CSRF 보호 비활성화
                .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))  // iframe 허용
                .authorizeHttpRequests((authorizeRequests) ->  // 최신 메서드 사용
                        authorizeRequests
                                .requestMatchers("/*/**").permitAll()  // 모든 요청에 대해 접근 허용
                                .requestMatchers("/identity-verifications/**").permitAll()  // 특정 경로 허용
                                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // 메서드 호출로 필터 객체를 전달

        return http.build();
    }
}
