package org.devkirby.hanimman.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.*;

import javax.crypto.SecretKey;

@Log4j2
public class JWTUtil {

//    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static  String key = "asdfgh12345678901234567890123456";
    private static Key secretKey = Keys.hmacShaKeyFor(key.getBytes());  // String을 바이트 배열로 변환한 후 SecretKey 생성
    public static Key getSecretKey(){
        return secretKey;
    }

    public static String generateToken(Map<String, Object> valueMap, String addKey) {
        long expirationTime = 1000 * 10; // 15분 (밀리초 단위)


        // JWT 토큰 생성
        String jwtStr = Jwts.builder()
                .setHeaderParam("typ", "JWT")  // 헤더 설정
                .setClaims(valueMap)  // 페이로드 설정
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))  // 발행 시간 설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationTime).toInstant()))  // 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 서명 알고리즘과 SecretKey 설정
                .compact();  // JWT 토큰 생성

        return jwtStr;  // 생성된 JWT 반환
    }

    public static String generateRefreshToken(String codenum){
        long expirationTime = 60 * 24 * 7; // 7일

        return Jwts.builder()
                .setSubject(codenum)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationTime).toInstant()))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {

        Claims claim = null;

        try{
            claim = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

        }catch(MalformedJwtException malformedJwtException){
            throw new CustomJWTException("MalFormed");
        }catch(ExpiredJwtException expiredJwtException){
            System.out.println("왜... 이거 안오는...?");
            throw new CustomJWTException("Expired");
        }catch(InvalidClaimException invalidClaimException){
            throw new CustomJWTException("Invalid");
        }catch(JwtException jwtException){
            throw new CustomJWTException("JWTError");
        }catch(Exception e){
            throw new CustomJWTException("Error");
        }
        return claim;
    }


}

