package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PetitionResponseDto {
    private Long petitionId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private Integer likesCount;
    private Integer interestCount;
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