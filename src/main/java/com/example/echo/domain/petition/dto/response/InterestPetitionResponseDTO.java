package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Petition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestPetitionResponseDTO {
    private Long petitionId;
    private String title;
    private String content;
    private Integer interestCount;

    public InterestPetitionResponseDTO(Petition petition){
        this.petitionId = petition.getPetitionId();
        this.title = petition.getTitle();
        this.content = petition.getContent();
        this.interestCount = petition.getInterestCount();

    }
}
