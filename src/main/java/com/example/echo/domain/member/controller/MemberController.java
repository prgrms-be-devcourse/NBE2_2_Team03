package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.service.MemberService;
import com.example.echo.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userId, @RequestParam String password) {
        String token = memberService.login(userId, password);
        return ResponseEntity.ok(token);
    }

    //회원 등록
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest memberRequest) {
        MemberResponse createdMember = memberService.createMember(memberRequest);
        return ResponseEntity.ok(ApiResponse.success(createdMember));
    }

    //id로 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long memberId) {
        MemberResponse memberDto = memberService.getMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(memberDto));
    }

    //회원 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    // 회원 수정

    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }

    // 회원 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // UserId로 찾기
    @GetMapping("/{userId}")
    public ResponseEntity<MemberDto> getMemberByUserId(@PathVariable String userId){
        MemberDto memberDto = memberService.findByUserId(userId);
        return ResponseEntity.ok(memberDto);
    }

    // 프로필 사진 조회
    @GetMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<String>> getAvatar(@PathVariable Long id) {
        String avatarUrl = memberService.getAvatar(id);
        return ResponseEntity.ok(ApiResponse.success(avatarUrl));
    }

    // 프로필 사진 업로드
    @PostMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<MemberResponse>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("avatarImage") MultipartFile avatarImage) {
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        requestDto.setAvatarImage(avatarImage);

        MemberResponse responseDto = memberService.updateAvatar(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}

