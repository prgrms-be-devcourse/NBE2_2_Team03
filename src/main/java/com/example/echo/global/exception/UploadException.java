package com.example.echo.global.exception;

public class UploadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // 사용자 정의 메시지를 전달하는 생성자
    public UploadException(String message) {
        super(message);
    }

    // 기본 생성자
    public UploadException() {
        super("파일 업로드 중 오류가 발생했습니다.");
    }

    // 특정 원인과 메시지를 전달하는 생성자
    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
