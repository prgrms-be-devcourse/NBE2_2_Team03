package com.example.echo.domain.petition.exception;

import lombok.Getter;

@Getter
public class PetitionNotFoundException extends RuntimeException {

    public PetitionNotFoundException(Long petitionId) {
        super("청원을 찾을 수 없습니다. ID: " + petitionId);
    }
}
