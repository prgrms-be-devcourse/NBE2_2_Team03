package com.example.echo.domain.member.dto.request;

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

    @NotNull(message = "파일은 비어있을 수 없습니다.")
    private MultipartFile avatarImage;
}