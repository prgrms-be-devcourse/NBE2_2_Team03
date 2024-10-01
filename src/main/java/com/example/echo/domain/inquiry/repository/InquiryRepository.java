package com.example.echo.domain.inquiry.repository;

import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryPaging {

    // ADMIN 회원의 모든 1:1 문의 리스트 페이징
    @Query("select i from Inquiry i join fetch i.member im")
    Page<InquiryResponseDTO> findAllInquiriesAdmin(Pageable pageable);
}