package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PetitionDetailResponseDto {

    @Schema(description = "청원의 ID", example = "1")
    private Long petitionId;

    @Schema(description = "청원을 생성한 회원의 ID", example = "1")
    private Long memberId;

    @Schema(description = "청원 제목", example = "독도의 날(10월 25일), 국가기념일 지정에 관한 청원")
    private String title;

    @Schema(description = "청원 내용", example = "1. 독도가 위험해지고 있습니다...")
    private String content;

    @Schema(description = "청원 요약", example = "독도 보호에 대한 간략한 요약")
    private String summary;

    @Schema(description = "청원 시작일", example = "2024-10-01T12:00:00")
    private LocalDateTime startDate;

    @Schema(description = "청원 종료일", example = "2024-10-31T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "청원 카테고리", example = "DIPLOMACY")
    private Category category;

    @Schema(description = "원본 URL", example = "https://petitions.assembly.go.kr/proceed/onGoingAll/20D5FC4DDB8625D7E064B49691C6967B")
    private String originalUrl;

    @Schema(description = "관련 뉴스", example = "https://www.newspenguin.com/news/articleView.html?idxno=16159")
    private String relatedNews;

    @Schema(description = "좋아요 수", example = "150")
    private Integer likesCount;

    @Schema(description = "관심 수", example = "200")
    private Integer interestCount;

    @Schema(description = "동의 수", example = "20527")
    private Integer agreeCount;

    public PetitionDetailResponseDto(Petition petition) {
        this.petitionId = petition.getPetitionId();
        this.memberId = (petition.getMember() != null) ? petition.getMember().getMemberId() : null; // Null 체크
        this.title = petition.getTitle();
        this.content = petition.getContent();
        this.summary = petition.getSummary();
        this.startDate = petition.getStartDate();
        this.endDate = petition.getEndDate();
        this.category = petition.getCategory();
        this.originalUrl = petition.getOriginalUrl();
        this.relatedNews = petition.getRelatedNews();
        this.likesCount = petition.getLikesCount();
        this.interestCount = petition.getInterestCount();
        this.agreeCount = petition.getAgreeCount();
    }
}
