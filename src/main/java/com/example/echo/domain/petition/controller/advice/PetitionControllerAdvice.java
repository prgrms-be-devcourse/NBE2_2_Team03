package com.example.echo.domain.petition.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
@ControllerAdvice
@Log4j2
public class PetitionControllerAdvice {

    // 잘못된 요청 데이터가 들어왔을 때 400 Bad Request 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("잘못된 요청: " + ex.getMessage());
    }

    // 해당하는 데이터를 찾을 수 없을 때 404 Not Found 예외 처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("해당 데이터를 찾을 수 없습니다: " + ex.getMessage());
    }

    // 500 Internal Server Error 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다: " + ex.getMessage());
    }

    // 특정 비즈니스 로직에 맞춘 예외 처리도 가능
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body("오류: " + ex.getReason());
    }
}

