package com.example.echo.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ProfileImageUpdateRequest {

    @Schema(description = "업데이트할 프로필 이미지 파일", required = true)
    @NotNull(message = "파일은 비어있을 수 없습니다.")
    private MultipartFile avatarImage;
}