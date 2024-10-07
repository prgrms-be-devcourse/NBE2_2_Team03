package com.example.echo.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAnswerRequest {

    @NotBlank(message = "답변 내용을 입력해주세요.")
    private String replyContent;
}
