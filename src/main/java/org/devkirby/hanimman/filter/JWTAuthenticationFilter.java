package org.devkirby.hanimman.filter;

import java.io.IOException;
import java.time.Instant;

import org.devkirby.hanimman.service.UserService;
import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JWTAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURL().toString().startsWith("/verification")
                || request.getRequestURL().toString().startsWith("/users/verify")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = JWTUtil.validateToken(token);
            String codenum = claims.get("codenum", String.class);

            if (codenum != null) {
                var customUserDetails = userService.loadUserByCodeNum(codenum);

                if (customUserDetails == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: User not found");
                    return;
                }

                // 계정 상태 확인
                Instant blockedAt = customUserDetails.getBlockedAt();
                Instant deletedAt = customUserDetails.getDeletedAt();
                Instant now = Instant.now();

                if (blockedAt != null && blockedAt.isBefore(now)) {
                    writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Account is blocked.");
                    return;
                }
                if (deletedAt != null && deletedAt.isBefore(now)) {
                    writeErrorResponse(response, HttpServletResponse.SC_GONE, "Account is deleted.");
                    return;
                }

                // 인증 성공
                var authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            // 만료된 토큰 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 만료되었습니다.");
            SecurityContextHolder.clearContext(); // 인증 정보를 초기화
            return;
        } catch (BlockedUserException e) {
            // 블록된 사용자 처리
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Blocked user: Access denied");
            return;
        } catch (Exception e) {
            // JWT 토큰이 유효하지 않은 경우
            e.printStackTrace();
            response.getWriter().write("Authentication failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 인증 완료 후 필터 체인 진행
        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
