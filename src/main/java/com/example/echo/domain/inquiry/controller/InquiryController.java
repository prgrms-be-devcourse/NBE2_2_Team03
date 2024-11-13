package com.example.echo.domain.inquiry.controller;

import com.example.echo.domain.inquiry.dto.request.AdminAnswerRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryCreateRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryPageRequest;
import com.example.echo.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.example.echo.domain.inquiry.dto.response.InquiryResponse;
import com.example.echo.domain.inquiry.service.InquiryService;
import com.example.echo.global.api.ApiResponse;
import com.example.echo.global.security.auth.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Inquiry Controller", description = "1:1 문의 관련 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "1:1 문의 등록", description = "사용자가 1:1 문의를 등록합니다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<InquiryResponse>> registerInquiry(
            @Parameter(description = "문의 등록 요청 데이터", required = true) @Valid @RequestBody InquiryCreateRequest inquiryRequest,
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse registeredInquiry = inquiryService.createInquiry(inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(registeredInquiry));
    }

    @Operation(summary = "1:1 문의 단건 조회", description = "사용자 또는 관리자가 1:1 문의를 조회합니다.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponse>> getInquiry(
            @Parameter(description = "조회할 문의 ID", required = true) @PathVariable Long inquiryId,
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse foundInquiry = inquiryService.getInquiryById(inquiryId, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(foundInquiry));
    }

    @Operation(summary = "1:1 문의 전체 리스트 조회", description = "사용자 또는 관리자가 모든 1:1 문의 리스트를 조회합니다.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InquiryResponse>>> getAllInquiries(
            @Parameter(description = "페이지 요청 데이터", required = true) @Valid @ModelAttribute InquiryPageRequest inquiryRequest,
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        Page<InquiryResponse> inquiriesPage = inquiryService.getInquiriesByMemberRole(inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(inquiriesPage));
    }

    @Operation(summary = "1:1 문의 수정", description = "사용자가 1:1 문의를 수정합니다.")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<InquiryResponse>> updateInquiry(
            @Parameter(description = "수정할 문의 ID", required = true) @PathVariable Long inquiryId,
            @Parameter(description = "문의 수정 요청 데이터", required = true) @Valid @RequestBody InquiryUpdateRequest inquiryRequest,
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        InquiryResponse updatedInquiry = inquiryService.updateInquiry(inquiryId, inquiryRequest, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(updatedInquiry));
    }

    @Operation(summary = "1:1 문의 삭제", description = "사용자 또는 관리자가 본인의 1:1 문의를 삭제합니다.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(
            @Parameter(description = "삭제할 문의 ID", required = true) @PathVariable Long inquiryId,
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        inquiryService.deleteInquiry(inquiryId, principal.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "관리자 답변 등록", description = "관리자가 1:1 문의에 답변을 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<ApiResponse<InquiryResponse>> addAnswer(
            @Parameter(description = "답변할 문의 ID", required = true) @PathVariable Long inquiryId,
            @Parameter(description = "답변 등록 요청 데이터", required = true) @Valid @RequestBody AdminAnswerRequest inquiryRequest) {
        inquiryService.addAnswer(inquiryId, inquiryRequest.getReplyContent());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
