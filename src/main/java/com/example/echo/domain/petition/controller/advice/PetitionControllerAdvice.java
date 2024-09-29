package com.example.echo.domain.petition.controller.advice;

import com.example.echo.domain.petition.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = "com.example.echo.domain.petition")
@Log4j2
public class PetitionControllerAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(PetitionNotFoundException.class)
    public ResponseEntity<?> handlePetitionNotFoundException(PetitionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedPetitionActionException.class)
    public ResponseEntity<?> handleUnauthorizedPetitionActionException(UnauthorizedPetitionActionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        log.error("청원 처리 중 예상치 못한 오류 발생: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "청원 처리 중 오류가 발생했습니다."));
    }
}
