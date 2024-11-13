package com.example.echo.domain.inquiry.dto.response;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import com.example.echo.domain.inquiry.entity.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponse {

    @Schema(description = "문의 ID", example = "1")
    private Long inquiryId;

    @Schema(description = "회원 ID", example = "user1")
    private Long memberId;

    @Schema(description = "문의 카테고리", example = "MEMBER")
    private InquiryCategory inquiryCategory;

    @Schema(description = "문의 제목", example = "문의 제목 예시")
    private String inquiryTitle;

    @Schema(description = "문의 내용", example = "문의 내용 예시")
    private String inquiryContent;

    @Schema(description = "문의 작성일", example = "2024-10-08T12:34:56")
    private LocalDateTime createdDate;

    @Schema(description = "답변 내용", example = "답변 내용 예시")
    private String replyContent;

    @Schema(description = "문의 상태", example = "RESOLVED")
    private InquiryStatus inquiryStatus;

    @Schema(description = "답변 작성일", example = "2024-10-08T14:56:00")
    private LocalDateTime repliedDate;

    public static InquiryResponse from(Inquiry inquiry) {
        return InquiryResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .memberId(inquiry.getMember().getMemberId())
                .inquiryCategory(inquiry.getInquiryCategory())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .createdDate(inquiry.getCreatedDate())
                .replyContent(inquiry.getReplyContent())
                .inquiryStatus(inquiry.getInquiryStatus())
                .repliedDate(inquiry.getRepliedDate())
                .build();
    }
}
