package com.example.echo.domain.member.controller.advice;

import com.example.echo.global.exception.UploadException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
@Log4j2
public class FileControllerAdvice {

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<?> handleException(UploadException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleException(MaxUploadSizeExceededException e) {
        log.error("파일 크기 제한 초과: " + e.getMessage());  // 로그로 예외 기록
        return ResponseEntity.badRequest().body(Map.of("error", "파일 크기 제한 초과"));
    }
}
