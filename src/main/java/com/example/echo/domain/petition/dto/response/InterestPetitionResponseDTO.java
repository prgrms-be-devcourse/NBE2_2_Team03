package com.example.echo.domain.petition.dto.response;

import com.example.echo.domain.petition.entity.Petition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestPetitionResponseDTO {

    @Schema(description = "청원의 ID", example = "1")
    private Long petitionId;

    @Schema(description = "청원 제목", example = "독도의 날(10월 25일), 국가기념일 지정에 관한 청원")
    private String title;

    @Schema(description = "청원 내용", example = "1. 독도가 위험해지고 있습니다...")
    private String content;

    @Schema(description = "관심 수", example = "150")
    private Integer interestCount;

    public InterestPetitionResponseDTO(Petition petition){
        this.petitionId = petition.getPetitionId();
        this.title = petition.getTitle();
        this.content = petition.getContent();
        this.interestCount = petition.getInterestCount();
    }
}
