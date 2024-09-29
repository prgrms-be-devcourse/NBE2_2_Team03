package com.example.echo.domain.petition.exception;

public class PetitionValidationException extends RuntimeException {
    public PetitionValidationException(String message) {
        super(message);
    }
}
