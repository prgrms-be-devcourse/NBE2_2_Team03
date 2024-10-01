package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberLoginRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.service.MemberService;
import com.example.echo.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> loginMember(@RequestBody MemberLoginRequest memberRequest) {
        Map<String, String> token = memberService.login(memberRequest);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    // 회원 등록
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        return ResponseEntity.ok(ApiResponse.success(createdMember));
    }

    // 관리자 memberId로 회원 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long memberId) {
        MemberResponse memberDto = memberService.getMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(memberDto));
    }

    // 관리자 회원 전체 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    // 회원 수정
    @PreAuthorize("authentication.principal.memberId == #memberId")   // 해당 userId만 접근 가능
    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }

    // 관리자 회원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 회원 프로필 사진 조회
    @PreAuthorize("authentication.principal.memberId == #memberId")   // 해당 memberId만 접근 가능
    @GetMapping("/{memberId}/avatar")
    public ResponseEntity<ApiResponse<String>> getAvatar(@PathVariable Long memberId) {
        String avatarUrl = memberService.getAvatar(memberId);
        return ResponseEntity.ok(ApiResponse.success(avatarUrl));
    }

    // 회원 프로필 사진 업로드
    @PreAuthorize("authentication.principal.memberId == #memberId")   // 해당 memberId만 접근 가능
    @PostMapping("/{memberId}/avatar")
    public ResponseEntity<ApiResponse<MemberResponse>> uploadAvatar(
            @PathVariable Long memberId,
            @RequestParam("avatarImage") MultipartFile avatarImage) {
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        requestDto.setAvatarImage(avatarImage);

        MemberResponse responseDto = memberService.updateAvatar(memberId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}

