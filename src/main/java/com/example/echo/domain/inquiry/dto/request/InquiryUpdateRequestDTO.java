package com.example.echo.domain.inquiry.dto.request;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryUpdateRequestDTO {
    private InquiryCategory inquiryCategory;
    private String inquiryTitle;
    private String inquiryContent;

    public void updateInquiry(Inquiry inquiry){
        inquiry.setInquiryCategory(this.inquiryCategory);
        inquiry.setInquiryTitle(this.inquiryTitle);
        inquiry.setInquiryContent(this.inquiryContent);
    }
}




