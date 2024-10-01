package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.PetitionCrawl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PetitionCrawlResponseDto {
    @Schema(description = "크롤링된 청원 리스트", example = "[{...}]")
    private List<PetitionCrawl> petitions; // 청원 데이터 리스트
}

