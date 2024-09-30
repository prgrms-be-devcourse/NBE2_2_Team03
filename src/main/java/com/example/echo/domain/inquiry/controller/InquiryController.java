package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

