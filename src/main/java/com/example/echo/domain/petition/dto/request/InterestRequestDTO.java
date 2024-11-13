package com.example.echo.domain.petition.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterestRequestDTO {

    @Schema(description = "청원의 ID", example = "1")
    private Long petitionId;

    @Schema(description = "회원의 ID", example = "1")
    private Long memberId;
}
