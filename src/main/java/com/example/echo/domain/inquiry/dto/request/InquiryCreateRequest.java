package com.example.echo.domain.inquiry.dto.request;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import com.example.echo.domain.member.entity.Member;
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
public class InquiryCreateRequest {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "문의 카테고리를 선택해주세요.")
    @Schema(description = "문의 카테고리", example = "MEMBER")
    private InquiryCategory inquiryCategory;

    @NotBlank(message = "문의 제목을 입력해주세요.")
    @Schema(description = "문의 제목", example = "로그인 실패에 대한 문의")
    private String inquiryTitle;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    @Schema(description = "문의 내용", example = "로그인이 되지 않습니다.")
    private String inquiryContent;

    public Inquiry toEntity(Member member) {
        return Inquiry.builder()
                .member(member)
                .inquiryCategory(inquiryCategory)
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .build();
    }
}
