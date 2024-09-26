package com.example.nbe2_2_team03.dto.response;

import com.example.nbe2_2_team03.entity.Category;
import com.example.nbe2_2_team03.entity.Member;
import com.example.nbe2_2_team03.entity.Petition;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PetitionResponseDto {
    private final Long petitionId;
    private final Member member;
    private final String title;
    private final String content;
    private final String summary;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Category category;
    private final String originalUrl;
    private final String relatedNews;
    private final Integer likesCount;
    private final Integer interestCount;
    private final Integer agreeCount;

    public PetitionResponseDto(Petition petition) {
        this.petitionId = petition.getPetitionId();
        this.member = petition.getMember();
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