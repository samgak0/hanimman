package org.devkirby.hanimman.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.devkirby.hanimman.service.UserService;
import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final Key key = JWTUtil.getSecretKey();
    private final UserService userService;

    @Autowired
    public JWTAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("doFilterInternal");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshHeader = request.getHeader("Refresh-Token");
        System.out.println(authHeader);
        System.out.println(refreshHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (refreshHeader != null) {
                handleRefreshToken(request, response, refreshHeader);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("token" + token);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println(claims);
            String codenum = claims.getSubject();

            if (codenum != null) {
                var customUserDetails = userService.loadUserByCodeNum(codenum);

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
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired.");
            return;
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) throws IOException {
        try {
            Claims refreshClaims = JWTUtil.validateToken(refreshToken);
            String codenum = refreshClaims.getSubject();

            if (codenum != null) {
                var customUserDetails = userService.loadUserByCodeNum(codenum);

                if (customUserDetails != null) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", "user");
                    claims.put("nickName", customUserDetails.getNickname());
                    claims.put("id", customUserDetails.getId());

                    String addKey = customUserDetails.getId() + customUserDetails.getCodenum() + customUserDetails.getCreatedAt();
                    String newAccessToken = JWTUtil.generateToken(claims, addKey);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"accessToken\": \"" + newAccessToken + "\"}");
                    return;
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token.");
                }
            }
        } catch (ExpiredJwtException e) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Refresh token has expired.");
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token.");
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
