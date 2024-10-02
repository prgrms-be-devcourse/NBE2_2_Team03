package com.example.echo.global.config;

import com.example.echo.global.security.filter.JWTCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final JWTCheckFilter jwtCheckFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureHttpSecurity(http);
        addJwtFilter(http);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = createCorsConfiguration();
        return createCorsConfigurationSource(corsConfig);
    }

    // HTTP 보안 설정
    private void configureHttpSecurity(HttpSecurity http) throws Exception {
        http
                .formLogin(login -> login.disable())    // 기본 폼 로그인 비활성화
                .logout(logout -> logout.disable())     // 로그아웃 기능 비활성화
                .csrf(csrf -> csrf.disable())   // CSRF 보호 비활성화
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.NEVER));    // 세션 사용 비활성화
    }

    // JWT 필터 추가
    private void addJwtFilter(HttpSecurity http) {
        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);   // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
    }

    // CORS 설정
    private CorsConfiguration createCorsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        corsConfig.setAllowCredentials(true);
        return corsConfig;
    }

    // CORS 설정 소스 반환
    private CorsConfigurationSource createCorsConfigurationSource(CorsConfiguration corsConfig) {
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfig); // 모든 요청 경로에 CORS 설정 등록
        return corsSource;
    }
}
