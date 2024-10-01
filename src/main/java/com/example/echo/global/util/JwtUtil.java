package com.example.echo.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;  // application.properties에서 비밀키를 주입

    @Value("${jwt.token-validity-in-seconds}")
    private long EXPIRATION_TIME;  // 유효기간 (초 단위)

    // JWT 생성
    public String generateToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME * 1000); // 밀리초 변환

        return Jwts.builder()
                .setSubject(userId)  // 사용자 이름을 subject로 설정
                .setIssuedAt(now)  // 토큰 발급 시간
                .setExpiration(validity)  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)  // 서명 알고리즘과 비밀 키 설정
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰 만료 확인
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // JWT 검증
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)  // 비밀키를 사용하여 JWT 파싱
                .parseClaimsJws(token)
                .getBody();
    }
}