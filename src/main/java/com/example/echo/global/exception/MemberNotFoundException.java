package com.example.echo.global.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String memberId) {
        super("회원을 찾을 수 없습니다. ID: " + memberId);
    }
}