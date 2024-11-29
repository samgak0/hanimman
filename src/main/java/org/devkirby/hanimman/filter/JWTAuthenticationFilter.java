package org.devkirby.hanimman.filter;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.devkirby.hanimman.service.UserService;
import org.devkirby.hanimman.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private final Key secretKey = JWTUtil.getSecretKey();
    private final UserService userService;

    @Autowired
    public JWTAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("doFilterInternal");

        if (request.getRequestURL().toString().startsWith("/verification")
                || request.getRequestURL().toString().startsWith("/users/verify")) {
            filterChain.doFilter(request, response);
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshHeader = request.getHeader("Refresh-Token");

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
            Claims claims = JWTUtil.validateToken(token);
            String codenum = claims.get("codenum",String.class);
            System.out.println(codenum);



            if (codenum != null) {
                var customUserDetails = userService.loadUserByCodeNum(codenum);
                System.out.println(customUserDetails);

                if (customUserDetails == null) {
                    System.out.println("[JWT 필터] 사용자 정보가 null입니다. codenum: " + codenum);
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
                System.out.println("인증 객체 생성 및 SecurityContext 설정 시도...");
                var authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("SecurityContext에 인증 정보 설정 완료!");
            }
        } catch (BlockedUserException e) {
            // 블록된 사용자일 경우, 인증 실패
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Blocked user: Access denied");
        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 만료되었습니다.");
        }
        catch (Exception e) {
            // JWT 토큰이 유효하지 않은 경우
            System.out.println("[JWT 필터] 예외 발생: " + e.getMessage());
            e.printStackTrace();
//            SecurityContextHolder.clearContext();
            response.getWriter().write("Authentication failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 7. 필터 체인 통과
        System.out.println("[JWT 필터] 인증 및 검증 완료, 다음 필터로 전달");
        filterChain.doFilter(request, response);
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken)
            throws IOException {
        try {
            Claims refreshClaims = JWTUtil.validateToken(refreshToken);
            String codenum = refreshClaims.getSubject();


            if (codenum != null) {
                var customUserDetails = userService.loadUserByCodeNum(codenum);
                System.out.println("customUserDetails");
                System.out.println(customUserDetails);

                if (customUserDetails != null) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", "user");
                    claims.put("nickName", customUserDetails.getNickname());
                    claims.put("id", customUserDetails.getId());

                    String addKey = customUserDetails.getId() + customUserDetails.getCodenum()
                            + customUserDetails.getCreatedAt();
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
