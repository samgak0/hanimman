package org.devkirby.hanimman.config;

import jakarta.servlet.Filter;
import org.devkirby.hanimman.filter.JWTAuthenticationFilter;
import org.devkirby.hanimman.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    // 생성자 주입 (UserService와 JWTAuthenticationFilter를 생성자에서 주입)
    @Autowired
    public SecurityConfig(UserService userService, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) -> csrfConfig.disable())  // CSRF 보호 비활성화
                .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))  // iframe 허용
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/*/**").permitAll()  // 모든 요청에 대해 접근 허용
                                .requestMatchers("/identity-verifications/**").permitAll()  // 특정 경로 허용
                                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // JWT 필터를 보안 필터 체인에 추가

        return http.build();
    }
}
