package com.example.echo.domain.inquiry.dto.request;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import com.example.echo.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDTO {

    private Long memberId;
    private InquiryCategory inquiryCategory;
    @NotBlank(message = "문의 제목을 입력해주세요.")
    private String inquiryTitle;
    @NotBlank(message = "문의 내용을 입력해주세요.")
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
