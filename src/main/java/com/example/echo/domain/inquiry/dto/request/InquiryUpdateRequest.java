package com.example.echo.domain.inquiry.dto.request;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryUpdateRequest {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "문의 카테고리를 선택해주세요.")
    @Schema(description = "문의 카테고리", example = "PETITION")
    private InquiryCategory inquiryCategory;

    @NotBlank(message = "문의 제목을 입력해주세요.")
    @Schema(description = "문의 제목", example = "청원이 보이지 않는 문제.")
    private String inquiryTitle;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    @Schema(description = "문의 내용", example = "청원 내용이 보이지 않습니다.")
    private String inquiryContent;

    public void updateInquiry(Inquiry inquiry) {
        inquiry.setInquiryCategory(this.inquiryCategory);
        inquiry.setInquiryTitle(this.inquiryTitle);
        inquiry.setInquiryContent(this.inquiryContent);
    }
}
