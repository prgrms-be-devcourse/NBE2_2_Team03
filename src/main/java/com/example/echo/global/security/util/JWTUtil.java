package com.example.echo.global.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    // 서명에 사용할 비밀 키 - 30자 이상으로 설정. 하드코딩된 키는 보안상 취약. 추후 수정
    private static final String key = "1234567890123456789012345678901234567890";

    // JWT 서명을 위한 비밀 키 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 문자열 생성. valueMap: JWT에 저장할 클레임 (payload), min: 만료 시간 (분 단위)
    public String createToken(Map<String, Object> valueMap, int min) {
        SecretKey key = getSigningKey();
        Date now = new Date(); // 토큰 발행 시간

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(min).toMillis()))
                .setClaims(valueMap)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰의 서명을 검증하고 클레임 반환
    public Map<String, Object> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
