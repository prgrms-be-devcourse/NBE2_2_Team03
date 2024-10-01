package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.InquiryPageRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // USER 회원 1:1 문의 등록
    @PostMapping
    public ResponseEntity<InquiryResponseDTO> registerInquiry(@RequestBody InquiryRequestDTO inquiryRequestDTO) {
        InquiryResponseDTO registeredInquiry = inquiryService.createInquiry(inquiryRequestDTO);
        return ResponseEntity.ok(registeredInquiry);
    }

    // 모든 회원 1:1 문의 단건 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDTO> getInquiry(@PathVariable Long inquiryId) {
        InquiryResponseDTO foundInquiry = inquiryService.getInquiryById(inquiryId);
        return ResponseEntity.ok(foundInquiry);
    }

    // ADMIN/USER 회원 종류에 따른 모든 1:1 문의 전체 리스트 조회
    @GetMapping
    public ResponseEntity<Page<InquiryResponseDTO>> getAllInquiries(
            @RequestParam Long memberId,    // 임시 url 부여. 로그인 기능 추가 시 Authentication으로 대체
            @ModelAttribute InquiryPageRequestDTO inquiryPageRequestDTO) {
        Page<InquiryResponseDTO> inquiriesPage = inquiryService.getInquiriesByMemberRole(memberId, inquiryPageRequestDTO);
        return ResponseEntity.ok(inquiriesPage);
    }
}

