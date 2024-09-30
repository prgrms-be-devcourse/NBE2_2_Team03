package com.example.echo.domain.petition.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long memberId) {
        super("회원을 찾을 수 없습니다. ID: " + memberId);
    }
}
