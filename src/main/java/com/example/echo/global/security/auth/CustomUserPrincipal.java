package com.example.echo.global.security.auth;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomUserPrincipal implements Principal {

    private final String userId;

    // 사용자 ID 반환
    @Override
    public String getName() {
        return this.userId;
    }
}
