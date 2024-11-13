package com.example.echo.global.advice;

import com.example.echo.global.api.ApiResponse;
import com.example.echo.global.exception.ErrorResponse;
import com.example.echo.global.exception.PetitionCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 오류가 발생했습니다."));
    }


    @ExceptionHandler(PetitionCustomException.class)
    public ResponseEntity<ErrorResponse> handleArgsException(PetitionCustomException e) {
        ErrorResponse response = ErrorResponse.from(e.getErrorCode().getHttpStatus(), e.getMessage());
        return ResponseEntity.status(response.getHttpStatus())
                .body(response);
    }
}
