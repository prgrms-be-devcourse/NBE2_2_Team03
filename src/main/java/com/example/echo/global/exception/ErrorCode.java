package com.example.echo.global.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
    USER_NOT_MEMBER(FORBIDDEN, "비회원은 사용할 수 없습니다."),

    // Petition
    PETITION_NOT_FOUND(NOT_FOUND, "청원을 찾을 수 없습니다."),
    SELENIUM_TIMEOUT(BAD_REQUEST, "크롤링 도중 시간 초과가 발생했습니다."),
    SELENIUM_NO_ELEMENT_FOUND(NOT_FOUND, "페이지에서 필요한 요소를 찾을 수 없습니다."),
    SELENIUM_UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 크롤링 오류가 발생했습니다."),

    // Inquiry
    INQUIRY_NOT_FOUND(NOT_FOUND, "1:1 문의를 찾을 수 없습니다."),
    INQUIRY_ACCESS_DENIED(FORBIDDEN, "1:1 문의에 대한 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
