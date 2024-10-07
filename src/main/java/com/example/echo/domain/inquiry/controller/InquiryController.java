package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.AdminAnswerRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryCreateRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryPageRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.example.echo.domain.inquiry.dto.response.InquiryResponse;
import com.example.echo.domain.inquiry.service.InquiryService;
import com.example.echo.global.api.ApiResponse;
import com.example.echo.global.security.auth.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // USER 회원 1:1 문의 등록
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<InquiryResponse>> registerInquiry(
            @Valid @RequestBody InquiryCreateRequest inquiryRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse registeredInquiry = inquiryService.createInquiry(inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(registeredInquiry));
    }

    // ADMIN/USER 회원 종류에 따른 1:1 문의 단건 조회
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponse>> getInquiry(
            @PathVariable Long inquiryId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse foundInquiry = inquiryService.getInquiryById(inquiryId, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(foundInquiry));
    }

    // ADMIN/USER 회원 종류에 따른 모든 1:1 문의 전체 리스트 조회
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InquiryResponse>>> getAllInquiries(
            @Valid @ModelAttribute InquiryPageRequest inquiryRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        Page<InquiryResponse> inquiriesPage = inquiryService.getInquiriesByMemberRole(inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(inquiriesPage));
    }

    // USER 회원 1:1 문의 수정
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponse>> updateInquiry(
            @PathVariable Long inquiryId,
            @Valid @RequestBody InquiryUpdateRequest inquiryRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse updatedInquiry = inquiryService.updateInquiry(inquiryId, inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(updatedInquiry));
    }

    // ADMIN/USER 본인 1:1 문의 삭제
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(
            @PathVariable Long inquiryId,
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        inquiryService.deleteInquiry(inquiryId, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ADMIN 관리자 답변
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<ApiResponse<InquiryResponse>> addAnswer(
            @PathVariable Long inquiryId,
            @Valid @RequestBody AdminAnswerRequest inquiryRequest) {
        inquiryService.addAnswer(inquiryId, inquiryRequest.getReplyContent());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}