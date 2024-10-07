package com.example.echo.global.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member
    MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(CONFLICT, "이미 존재하는 이메일입니다."),
    PHONE_ALREADY_EXISTS(CONFLICT, "이미 존재하는 전화번호입니다."),

    // Petition
    PETITION_NOT_FOUND(NOT_FOUND, "청원을 찾을 수 없습니다."),
    PETITION_NOT_MATCHED(BAD_REQUEST, "청원 정보가 일치하지 않습니다."),
    PETITION_NOT_REMOVED(CONFLICT, "청원 삭제에 실패했습니다."),
    PETITION_NOT_REGISTERED(CONFLICT, "청원 등록에 실패했습니다."),
    PETITION_NOT_MODIFIED(CONFLICT, "청원 수정에 실패했습니다."),

    // Inquiry
    INQUIRY_NOT_FOUND(NOT_FOUND, "1:1 문의를 찾을 수 없습니다."),
    INQUIRY_ACCESS_DENIED(FORBIDDEN, "1:1 문의에 대한 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
