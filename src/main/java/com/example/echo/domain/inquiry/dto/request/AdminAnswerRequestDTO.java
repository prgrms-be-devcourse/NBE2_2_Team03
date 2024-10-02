package com.example.echo.domain.inquiry.dto.request;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminAnswerRequestDTO {
    private Long inquiryId;
    private String replyContent;

}
