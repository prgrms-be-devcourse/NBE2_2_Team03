package com.example.echo.domain.petition.exception;

public class UnauthorizedPetitionActionException extends RuntimeException {
    public UnauthorizedPetitionActionException(String message) {
        super(message); // 추후 작성
    }
}