package com.example.echo.domain.petition.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PetitionCrawlRequestDto {
    @NotBlank(message = "크롤링할 URL은 필수입니다.") // URL 미입력시 처리
    @Schema(description = "크롤링할 청원 페이지의 URL", example = "https://petitions.assembly.go.kr/proceed/onGoingAll")
    private String url;

}
