
package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PetitionDetailResponseDto {
    private Long petitionId;
    private Long memberId;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private String originalUrl;
    private String relatedNews;
    private Integer likesCount;
    private Integer interestCount;
    private Integer agreeCount;

    public PetitionDetailResponseDto(Petition petition) {
        this.petitionId = petition.getPetitionId();
        this.memberId = petition.getMember().getMemberId();
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
