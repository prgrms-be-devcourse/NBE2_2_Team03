package com.example.echo.domain.petition.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterestRequestDTO {
    private Long petitionId;
    private Long memberId;
}
