package com.example.echo.domain.inquiry.repository;

import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryPaging {

    // USER 회원의 본인이 등록한 1:1 문의 리스트 페이징
    Page<InquiryResponseDTO> findAllInquiriesUser(Long memberId, Pageable pageable);
}
