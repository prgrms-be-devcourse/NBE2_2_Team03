package com.example.echo.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;

    public static ErrorResponse from(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus, message);
    }
}
