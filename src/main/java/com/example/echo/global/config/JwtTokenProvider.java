package com.example.echo.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private static String secretKey;  // application.properties에 설정된 secret key

    @Value("${jwt.token-validity-in-seconds}")
    private static long tokenValidityInSeconds;  // 토큰 유효 시간

    private final UserDetailsService userDetailsService;

    // 토큰 생성 메서드
    public static String createToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);  // 권한 정보 추가

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)  // 유효 기간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명 방식 및 키
                .compact();
    }

    // 토큰에서 사용자 정보 추출
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;  // 토큰이 유효하지 않으면 false 반환
        }
    }

    // 토큰에서 권한 정보 추출 후 인증 객체 반환
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserId(token));
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}