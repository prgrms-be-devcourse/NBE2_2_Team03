package com.example.echo.domain.petition.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
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
    private Long memberId;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private String originalUrl;
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
