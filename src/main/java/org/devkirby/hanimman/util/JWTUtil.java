package org.devkirby.hanimman.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.*;

import javax.crypto.SecretKey;

@Log4j2
public class JWTUtil {

    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static SecretKey getKey(){
        return key;
    }

    public static String generateToken(Map<String, Object> valueMap, String addKey) {
        long expirationTime = 1000 * 60 * 15; //15분

        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationTime).toInstant()))
                .signWith(key)
                .compact();

        return jwtStr;
    }

    public static String generateRefreshToken(String username){
        long expirationTime = 60 * 24 * 7; // 7일

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationTime).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try{
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

        }catch(MalformedJwtException malformedJwtException){
            throw new CustomJWTException("MalFormed");
        }catch(ExpiredJwtException expiredJwtException){
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

