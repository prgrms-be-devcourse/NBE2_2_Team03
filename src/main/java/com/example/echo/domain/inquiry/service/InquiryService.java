package com.example.echo.domain.inquiry.service;

import com.example.echo.domain.inquiry.dto.request.InquiryPageRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryUpdateRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.repository.InquiryRepository;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final MemberService memberService;
    private final InquiryRepository inquiryRepository;

    @Transactional
    public InquiryResponseDTO createInquiry(InquiryRequestDTO inquiryRequestDTO) {
        Member foundMember = memberService.findMemberById(inquiryRequestDTO.getMemberId());
        Inquiry createdInquiry = inquiryRequestDTO.toEntity(foundMember);   // 1:1 문의 생성
        Inquiry savedInquiry = inquiryRepository.save(createdInquiry);
        return InquiryResponseDTO.from(savedInquiry);
    }

    // 모든 회원 1:1 문의 단건 조회
    public InquiryResponseDTO getInquiryById(Long inquiryId) {
        Inquiry foundInquiry = findInquiryById(inquiryId);
        return InquiryResponseDTO.from(foundInquiry);
    }

    // ADMIN/USER 회원 종류에 따른 1:1 문의 전체 리스트 조회
    public Page<InquiryResponseDTO> getInquiriesByMemberRole(Long memberId, InquiryPageRequestDTO inquiryPageRequestDTO) {
        // memberId에 따라 해당 다르게 조회
        MemberResponse foundMember = memberService.getMember(memberId);
        if (foundMember.getRole() == Role.ADMIN) {
            return findAllForAdmin(inquiryPageRequestDTO.getPageable());    // ADMIN 모든 문의 조회
        } else {
            return findAllForUser(memberId, inquiryPageRequestDTO.getPageable());     // USER 개인 모든 문의 조회
        }
    }

    // 문의 ID로 문의 조회
    private Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("1:1 문의 정보를 찾을 수 없습니다."));
    }

    // ADMIN 모든 문의 조회
    private Page<InquiryResponseDTO> findAllForAdmin(Pageable pageable) {
        return inquiryRepository.findAllInquiriesAdmin(pageable);
    }

    // USER 본인 문의 조회
    private Page<InquiryResponseDTO> findAllForUser(Long memberId, Pageable pageable) {
        return inquiryRepository.findAllInquiriesUser(memberId, pageable);
    }

    //본인 1:1문의 수정
    @Transactional
    public InquiryResponseDTO updateInquiry (Long inquiryId, InquiryUpdateRequestDTO inquiryUpdateRequestDTO){
        Inquiry inquiry = findInquiryById(inquiryId);
        inquiryUpdateRequestDTO.updateInquiry(inquiry);

        return InquiryResponseDTO.from(inquiryRepository.save(inquiry));
    }

    //1:1 문의 삭제
    @Transactional
    public void deleteInquiry(Long inquiryId){
        inquiryRepository.delete(findInquiryById(inquiryId));
    }

    //관리자 답변
    @Transactional
    public void addAnswer(Long inquiryId, String replyContent){
        Inquiry inquiry = findInquiryById(inquiryId);

        inquiry.setReplyContent(replyContent);
        inquiryRepository.save(inquiry);
    }
}
