package com.example.echo.domain.inquiry.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryStatus {

    PENDING("답변대기중"),
    RESOLVED("답변완료");

    private final String description;
}
