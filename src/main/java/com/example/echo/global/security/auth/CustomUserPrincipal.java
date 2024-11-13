package com.example.echo.global.security.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomUserPrincipal implements Principal {

    private final String userId;

    @Getter
    private final Long memberId;    // memberId 반환

    // 사용자 ID 반환
    @Override
    public String getName() {
        return this.userId;
    }
}