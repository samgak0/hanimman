package org.devkirby.hanimman.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devkirby.hanimman.service.UserService;
import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey key = JWTUtil.getKey();
    private final UserService userService;

    @Autowired
    public JWTAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(header);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // JWT 토큰을 파싱하여 사용자 정보 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String codenum = claims.getSubject();

            if (codenum != null) {
                // 사용자 정보 (DTO) 로드
                var customUserDetails = userService.loadUserByCodeNum(codenum);

                // DTO에서 사용자 상태 확인 (블록 상태, 탈퇴 상태)
                Instant blockedAt = customUserDetails.getBlockedAt();
                Instant deletedAt = customUserDetails.getDeletedAt();

                // 계정이 블록되었거나 탈퇴된 경우 인증 실패 처리
                Instant now = Instant.now();
                if (blockedAt != null && blockedAt.isBefore(now)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 계정이 블록된 경우
                    return;
                }
                if (deletedAt != null && deletedAt.isBefore(now)) {
                    response.setStatus(HttpServletResponse.SC_GONE); // 계정이 탈퇴된 경우
                    return;
                }

                // 인증 성공 - 사용자 상태가 유효한 경우
                var authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (BlockedUserException e) {
            // 블록된 사용자일 경우, 인증 실패
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Blocked user: Access denied");
        } catch (Exception e) {
            // JWT 토큰이 유효하지 않은 경우
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }
}
