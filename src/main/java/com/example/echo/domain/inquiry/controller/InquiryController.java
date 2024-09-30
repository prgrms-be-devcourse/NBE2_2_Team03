package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
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

    // ADMIN 회원 모든 1:1 문의 전체 리스트 조회

    // USER 회원 본인이 등록한 1:1 문의 전체 리스트 조회
}

