package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberLoginRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.service.MemberService;
import com.example.echo.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member Controller", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    // 로그인 API
    @Operation(summary = "회원 로그인", description = "회원이 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> loginMember(
            @Parameter(description = "회원 로그인 요청 정보", required = true) @Valid @RequestBody MemberLoginRequest memberRequest) {
        Map<String, String> token = memberService.login(memberRequest);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    // 회원 등록
    @Operation(summary = "회원 등록", description = "신규 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Parameter(description = "신규 회원 등록 요청 정보", required = true) @Valid @RequestBody MemberCreateRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        return ResponseEntity.ok(ApiResponse.success(createdMember));
    }

    // 관리자 memberId로 회원 조회
    @Operation(summary = "회원 조회", description = "관리자가 회원 번호를 통해 회원 정보를 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(
            @Parameter(description = "조회할 회원의 ID", required = true) @PathVariable Long memberId) {
        MemberResponse foundMember = memberService.getMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(foundMember));
    }

    // 관리자 회원 전체 조회
    @Operation(summary = "회원 전체 조회", description = "관리자가 전체 회원 목록을 조회합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    // 회원 수정
    @Operation(summary = "회원 수정", description = "회원이 자신의 정보를 수정합니다.")
    @PreAuthorize("authentication.principal.memberId == #memberId")
    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @Parameter(description = "수정할 회원의 ID", required = true) @PathVariable Long memberId,
            @Parameter(description = "회원 수정 요청 정보", required = true) @Valid @RequestBody MemberUpdateRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }

    // 관리자 회원 삭제
    @Operation(summary = "회원 삭제", description = "관리자가 회원 정보를 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @Parameter(description = "삭제할 회원의 ID", required = true) @PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 회원 프로필 사진 조회
    @Operation(summary = "프로필 사진 조회", description = "회원이 자신의 프로필 사진을 조회합니다.")
    @PreAuthorize("authentication.principal.memberId == #memberId")
    @GetMapping("/{memberId}/avatar")
    public ResponseEntity<ApiResponse<String>> getAvatar(
            @Parameter(description = "조회할 회원의 ID", required = true) @PathVariable Long memberId) {
        String avatarUrl = memberService.getAvatar(memberId);
        return ResponseEntity.ok(ApiResponse.success(avatarUrl));
    }

    // 회원 프로필 사진 업로드
    @Operation(summary = "프로필 사진 업로드", description = "회원이 자신의 프로필 사진을 업로드합니다.")
    @PreAuthorize("authentication.principal.memberId == #memberId")
    @PostMapping("/{memberId}/avatar")
    public ResponseEntity<ApiResponse<MemberResponse>> uploadAvatar(
            @Parameter(description = "업로드할 회원의 ID", required = true) @PathVariable Long memberId,
            @Parameter(description = "업로드할 프로필 사진 파일", required = true) @RequestParam("avatarImage") MultipartFile avatarImage) {
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        requestDto.setAvatarImage(avatarImage);

        MemberResponse responseDto = memberService.updateAvatar(memberId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    // 보호된 데이터 요청
    @Operation(summary = "보호된 데이터 요청", description = "보호된 데이터를 요청합니다. 로그인된 회원 정보에 따라 조회 가능합니다.")
    @GetMapping("/protected-data")
    public ResponseEntity<ApiResponse<MemberResponse>> getProtectedData(
            @Parameter(description = "현재 인증된 사용자 정보", required = true) Authentication authentication) {
        MemberResponse foundMember = memberService.getMemberInfo(authentication);
        return ResponseEntity.ok(ApiResponse.success(foundMember));
    }
}
