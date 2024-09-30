package com.example.echo.domain.inquiry.service;

import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.repository.InquiryRepository;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final MemberService memberService;
    private final InquiryRepository inquiryRepository;

    @Transactional
    public InquiryResponseDTO createInquiry(InquiryRequestDTO inquiryRequestDTO) {
        Member foundMember = memberService.findMemberById(inquiryRequestDTO.getMemberId());    // memberId로 Member 엔티티를 조회
        Inquiry createdInquiry = inquiryRequestDTO.toEntity(foundMember);   // 1:1 문의 생성
        Inquiry savedInquiry = inquiryRepository.save(createdInquiry);
        return InquiryResponseDTO.from(savedInquiry);
    }
}
