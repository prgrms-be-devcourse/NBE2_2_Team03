package com.example.echo.global.util;

import com.example.echo.global.exception.UploadException;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class UploadUtil {

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempDir = new File(uploadPath);

        // 업로드 디렉토리가 없으면 생성
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadPath);
        }

        // 업로드 경로가 디렉토리인지 확인
        if (!tempDir.isDirectory()) {
            throw new RuntimeException("업로드 경로가 유효하지 않습니다: " + uploadPath);
        }

        uploadPath = tempDir.getAbsolutePath();
        log.info("--- uploadPath : " + uploadPath);
    }

    public String upload(MultipartFile file) {
        // 파일 타입 확인
        if (file.isEmpty() || !isImageFile(file)) {
            throw new UploadException("업로드할 파일은 이미지 파일이어야 합니다.");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadPath, fileName);

        try {
            file.transferTo(destinationFile); // 파일 저장
        } catch (IOException e) {
            throw new UploadException("파일 업로드에 실패했습니다.", e);
        }

        return destinationFile.getPath(); // 저장된 파일의 경로 반환
    }

    // 여러 파일 업로드
    public List<String> upload(MultipartFile[] files) {
        List<String> filenames = new ArrayList<>();

        for (MultipartFile file : files) {
            log.info("------------------");
            log.info("name : " + file.getName());
            log.info("origin name : " + file.getOriginalFilename());
            log.info("type : " + file.getContentType());

            // 파일 타입 확인
            if (!isImageFile(file)) {
                log.error("--- 지원하지 않는 파일 타입 : " + file.getOriginalFilename());
                throw new UploadException("모든 파일은 이미지 파일이어야 합니다: " + file.getOriginalFilename());
            }

            String uuid = UUID.randomUUID().toString();
            String saveFilename = uuid + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, saveFilename);

            try {
                saveFile(file, savePath);
                createThumbnail(savePath, uuid);
                filenames.add(saveFilename);
            } catch (IOException e) {
                log.error("파일 업로드 중 오류 발생: " + e.getMessage());
                throw new UploadException("파일 업로드 중 오류가 발생했습니다: " + file.getOriginalFilename(), e);
            }
        }
        return filenames;
    }

    private void saveFile(MultipartFile file, Path savePath) throws IOException {
        file.transferTo(savePath.toFile());
    }

    // 이미지 파일인지 확인하는 메서드
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // 썸네일 파일 생성
    private void createThumbnail(Path savePath, String uuid) {
        try {
            String originalFilename = savePath.getFileName().toString();
            String thumbnailFilename = "s_" + originalFilename;

            // 썸네일 파일 생성
            Thumbnails.of(new File(savePath.toString()))
                    .size(150, 150)
                    .toFile(new File(uploadPath, thumbnailFilename));

            log.info("썸네일 생성 성공: " + thumbnailFilename);
        } catch (IOException e) {
            log.error("썸네일 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // 업로드 파일 삭제
    public void deleteFile(String filename) {
        File file = new File(uploadPath, filename);
        String thumbFilename = "s_" + filename; // 썸네일 파일 이름 생성
        File thumbFile = new File(uploadPath, thumbFilename);

        try {
            if (file.exists() && file.delete()) {
                log.info("파일 삭제 성공: " + filename);
            } else {
                log.warn("삭제할 파일이 존재하지 않음: " + filename);
            }

            if (thumbFile.exists() && thumbFile.delete()) {
                log.info("썸네일 삭제 성공: " + thumbFilename);
            } else {
                log.warn("삭제할 썸네일 파일이 존재하지 않음: " + thumbFilename);
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
