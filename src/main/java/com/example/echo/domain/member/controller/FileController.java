package com.example.echo.domain.member.controller;

import com.example.echo.global.api.ApiResponse;
import com.example.echo.global.exception.UploadException;
import com.example.echo.global.util.UploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/files")
@Tag(name = "File Controller", description = "파일 업로드 및 삭제 관리  API")
public class FileController {

    private final UploadUtil uploadUtil;

    // 파일 업로드
    @Operation(summary = "파일 업로드", description = "파일을 업로드합니다.")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<String>>> uploadFile(
            @RequestParam("files") MultipartFile[] files) {
        log.info("--- uploadFile() invoked ---");

        // 업로드 파일이 없는 경우
        if (files.length == 0) {
            throw new UploadException("업로드 파일이 없습니다.");
        }

        // 각 파일에 대해 확장자 체크 및 로그 기록
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            log.debug("Checking file extension for: " + originalFilename);
            checkFileExt(originalFilename); // 확장자 체크
        }

        // 업로드 수행
        List<String> uploadedFiles = uploadUtil.upload(files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(uploadedFiles));
    }

    // 업로드 파일 확장자 체크
    public void checkFileExt(String filename) throws UploadException {
        if (filename == null || filename.isEmpty()) {
            throw new UploadException("파일 이름이 유효하지 않습니다.");
        }

        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        String regExp = "(?i)^(jpg|jpeg|png|gif|bmp)$";

        if (!ext.matches(regExp)) {
            throw new UploadException("지원하지 않는 파일 형식입니다: " + ext);
        }
    }

    // 파일 삭제
    @Operation(summary = "파일 삭제", description = "파일 이름으로 파일을 삭제합니다.")
    @DeleteMapping("/{filename}")
    public ResponseEntity<ApiResponse<?>> fileDelete(@PathVariable String filename) {
        log.info("--- fileDelete() invoked for filename: " + filename);

        try {
            // UploadUtil 클래스의 deleteFile 메서드 호출
            uploadUtil.deleteFile(filename);
            log.info("파일이 성공적으로 삭제되었습니다: " + filename);
            return ResponseEntity.ok(ApiResponse.success("파일이 성공적으로 삭제되었습니다.", null));
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("파일 삭제에 실패했습니다: " + e.getMessage()));
        }
    }

}
