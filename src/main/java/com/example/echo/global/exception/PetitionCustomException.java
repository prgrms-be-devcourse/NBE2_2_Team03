package com.example.echo.global.exception;

import lombok.Getter;

@Getter
public class PetitionCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public PetitionCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
