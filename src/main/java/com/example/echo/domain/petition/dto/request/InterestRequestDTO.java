package com.example.echo.domain.petition.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterestRequestDTO {
    private Long petitionId;
    private Long memberId;
}
