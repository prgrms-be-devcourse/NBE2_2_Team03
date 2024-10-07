package com.example.echo.domain.inquiry.service;

import com.example.echo.domain.inquiry.dto.request.InquiryCreateRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryPageRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.example.echo.domain.inquiry.dto.response.InquiryResponse;
import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.repository.InquiryRepository;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.service.MemberService;
import com.example.echo.global.exception.ErrorCode;
import com.example.echo.global.exception.PetitionCustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final MemberService memberService;
    private final InquiryRepository inquiryRepository;

    // USER 회원 1:1 문의 등록
    @Transactional
    public InquiryResponse createInquiry(InquiryCreateRequest inquiryRequest, Long memberId) {
        Member foundMember = memberService.findMemberById(memberId);
        Inquiry createdInquiry = inquiryRequest.toEntity(foundMember);
        Inquiry savedInquiry = inquiryRepository.save(createdInquiry);
        return InquiryResponse.from(savedInquiry);
    }

    // ADMIN/USER 회원 종류에 따른 1:1 문의 단건 조회
    public InquiryResponse getInquiryById(Long inquiryId, Long memberId) {
        MemberResponse foundMember = memberService.getMember(memberId);
        Inquiry foundInquiry = findInquiryById(inquiryId);
        if (foundMember.getRole() == Role.USER) {
            validateUserInquiryAccess(memberId, foundInquiry);
        }
        return InquiryResponse.from(foundInquiry);
    }

    // ADMIN/USER 회원 종류에 따른 1:1 문의 전체 리스트 조회
    public Page<InquiryResponse> getInquiriesByMemberRole(InquiryPageRequest inquiryRequest, Long memberId) {
        MemberResponse foundMember = memberService.getMember(memberId);
        if (foundMember.getRole() == Role.ADMIN) {
            return findAllForAdmin(inquiryRequest.getPageable());    // ADMIN 모든 문의 조회
        } else {
            return findAllForUser(memberId, inquiryRequest.getPageable());     // USER 개인 모든 문의 조회
        }
    }

    // USER 본인 1:1 문의 수정
    @Transactional
    public InquiryResponse updateInquiry(Long inquiryId, InquiryUpdateRequest inquiryRequest, Long memberId) {
        Inquiry foundInquiry = findInquiryById(inquiryId);
        validateUserInquiryAccess(memberId, foundInquiry);
        inquiryRequest.updateInquiry(foundInquiry);
        return InquiryResponse.from(inquiryRepository.save(foundInquiry));
    }

    // ADMIN/USER 본인 1:1 문의 삭제
    @Transactional
    public void deleteInquiry(Long inquiryId, Long memberId) {
        MemberResponse foundMember = memberService.getMember(memberId);
        Inquiry foundInquiry = findInquiryById(inquiryId);
        if (foundMember.getRole() == Role.USER) {
            validateUserInquiryAccess(memberId, foundInquiry);
        }
        inquiryRepository.delete(foundInquiry);
    }

    // 관리자 문의 답변
    @Transactional
    public void addAnswer(Long inquiryId, String replyContent) {
        Inquiry inquiry = findInquiryById(inquiryId);
        inquiry.changeReplyContent(replyContent);
        inquiryRepository.save(inquiry);
    }

    // 문의 ID로 문의 조회
    private Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new PetitionCustomException(ErrorCode.INQUIRY_NOT_FOUND));
    }

    // ADMIN 모든 문의 조회
    private Page<InquiryResponse> findAllForAdmin(Pageable pageable) {
        return inquiryRepository.findAllInquiriesAdmin(pageable);
    }

    // USER 본인 문의 조회
    private Page<InquiryResponse> findAllForUser(Long memberId, Pageable pageable) {
        return inquiryRepository.findAllInquiriesUser(memberId, pageable);
    }

    // USER인 경우 해당 문의 작성자 본인 검증
    private void validateUserInquiryAccess(Long memberId, Inquiry inquiry) {
        if (!Objects.equals(inquiry.getMember().getMemberId(), memberId)) {
            throw new PetitionCustomException(ErrorCode.INQUIRY_ACCESS_DENIED);
        }
    }

    // 문의ID와 멤버ID가 일치한지 확인
    public boolean isInquiryOwner(Long inquiryId, Long memberId) {
        Inquiry inquiry = findInquiryById(inquiryId);

        return inquiry.getMember().getMemberId().equals(memberId);
    }
}
