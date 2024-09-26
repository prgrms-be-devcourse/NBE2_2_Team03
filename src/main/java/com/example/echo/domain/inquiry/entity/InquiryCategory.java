package com.example.echo.domain.inquiry.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryCategory {

    MEMBER("회원정보"),
    PETITION("청원"),
    OTHERS("기타");

    private final String description;
}
