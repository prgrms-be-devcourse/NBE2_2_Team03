package com.example.echo.global.security.filter;

import com.example.echo.global.security.auth.CustomUserPrincipal;
import com.example.echo.global.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // 필터링 적용하지 않을 URI 체크
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if(!request.getRequestURI().startsWith("/api/")){ //이미지 경로를 제외하기 위해 api로 시작하지 않는 경로도 제외
            return true;
        }

        return isAuthExcludedPath(request); // 토큰 발급 경로는 제외
    }

    // 필터링 적용 - 액세스 토큰 확인
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String headerAuth = request.getHeader("Authorization");

        // 액세스 토큰 유효성 검사
        if (!isTokenValid(headerAuth)) {
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
            return;
        }

        // 토큰 유효성 검증 후 SecurityContext 설정
        String accessToken = headerAuth.substring(7); // "Bearer " 제외한 토큰 저장
        try {
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            setAuthentication(claims);  // 인증 정보 설정
            filterChain.doFilter(request, response); // 검증 결과 문제가 없는 경우 요청 처리
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    // 특정 URI가 인증 제외 경로인지 확인
    private boolean isAuthExcludedPath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 회원 가입/로그인 경로 인증 제외
        if (requestURI.startsWith("/api/members/login") || requestURI.startsWith("/api/members/signup")) {
            return true;
        }

        // 청원 단건/전체/만료일순 조회(GET)는 인증 제외
        if (requestURI.startsWith("/api/petitions") && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        return false;
    }

    // 액세스 토큰 유효성 검사
    private boolean isTokenValid(String headerAuth) {
        return headerAuth != null && headerAuth.startsWith("Bearer ");
    }

    // SecurityContext에 인증 정보 설정
    private void setAuthentication(Map<String, Object> claims) {
        String userId = claims.get("userId").toString();
        String[] roles = claims.get("role").toString().split(","); // role이 여러개일 수 있으므로
        Long memberId = Long.valueOf(claims.get("memberId").toString());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                new CustomUserPrincipal(userId, memberId),
                null, // 이미 인증되었으므로 null
                Arrays.stream(roles)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())
        );

        SecurityContextHolder.getContext().setAuthentication(authToken); // SecurityContext에 인증 정보 저장
    }

    // 403 Forbidden 에러 처리
    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
    }
}
