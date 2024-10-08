package com.example.echo.domain.petition.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetitionRequestDto {

    @Schema(description = "회원의 ID", example = "1")
    private Long memberId;

    @Schema(description = "청원 제목", example = "독도의 날(10월 25일), 국가기념일 지정에 관한 청원")
    private String title;

    @Schema(description = "청원 내용", example = "1. 독도가 위험해지고 있습니다...")
    private String content;

    @Schema(description = "청원 요약", example = "독도 보호에 대한 간략한 요약")
    private String summary;

    @Schema(description = "청원 시작일", example = "2024-10-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "청원 종료일", example = "2024-10-31T23:59:59")
    private LocalDateTime endDate;

    @Schema(description = "청원 카테고리", example = "DIPLOMACY")
    private Category category;

    @Schema(description = "원본 URL", example = "https://petitions.assembly.go.kr/proceed/onGoingAll/20D5FC4DDB8625D7E064B49691C6967B")
    private String originalUrl;

    @Schema(description = "관련 뉴스", example = "https://www.newspenguin.com/news/articleView.html?idxno=16159")
    private String relatedNews;

    public Petition toEntity(Member member) {
        return Petition.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .summary(this.summary)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .category(this.category)
                .originalUrl(this.originalUrl)
                .relatedNews(this.relatedNews)
                .build();
    }

    public Petition toEntityWithExistingData(Petition existingPetition, Member member) {
        return Petition.builder()
                .petitionId(existingPetition.getPetitionId()) // 기존 ID 유지
                .member(member)
                .title(this.title)
                .content(this.content)
                .summary(this.summary)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .category(this.category)
                .originalUrl(this.originalUrl)
                .relatedNews(this.relatedNews)
                .likesCount(existingPetition.getLikesCount()) // 기존 값 유지
                .interestCount(existingPetition.getInterestCount()) // 기존 값 유지
                .agreeCount(existingPetition.getAgreeCount()) // 기존 값 유지
                .build();
    }
}
