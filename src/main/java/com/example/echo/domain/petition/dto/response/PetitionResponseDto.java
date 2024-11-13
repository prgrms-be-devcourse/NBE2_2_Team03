package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PetitionResponseDto {

    @Schema(description = "청원의 ID", example = "1")
    private Long petitionId;

    @Schema(description = "청원 제목", example = "독도의 날(10월 25일), 국가기념일 지정에 관한 청원")
    private String title;

    @Schema(description = "청원 시작일", example = "2024-10-01T12:00:00")
    private LocalDateTime startDate;

    @Schema(description = "청원 종료일", example = "2024-10-31T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "청원 카테고리", example = "DIPLOMACY")
    private Category category;

    @Schema(description = "좋아요 수", example = "150")
    private Integer likesCount;

    @Schema(description = "관심 수", example = "200")
    private Integer interestCount;

    @Schema(description = "동의 수", example = "20527")
    private Integer agreeCount;

    public PetitionResponseDto(Petition petition) {
        this.petitionId = petition.getPetitionId();
        this.title = petition.getTitle();
        this.startDate = petition.getStartDate();
        this.endDate = petition.getEndDate();
        this.category = petition.getCategory();
        this.likesCount = petition.getLikesCount();
        this.interestCount = petition.getInterestCount();
        this.agreeCount = petition.getAgreeCount();
    }
}