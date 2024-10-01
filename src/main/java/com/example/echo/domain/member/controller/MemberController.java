package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.ProfileImageUpdateResponse;
import com.example.echo.domain.member.service.MemberService;
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberDto memberDto) {
        Map<String, String> token = memberService.login(memberDto);
        return ResponseEntity.ok(token);
    }

    // 회원 등록
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberDto memberDto) {
        // 기본 아바타 이미지 경로 설정
        memberDto.setAvatarImage("/images/avatar-default.png"); // static 폴더 내 이미지 경로
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.ok(createdMember);
    }

    // 관리자 memberId로 회원 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
        MemberDto memberDto = memberService.getMember(memberId);
        return ResponseEntity.ok(memberDto);
    }

    // 관리자 회원 전체 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 수정
    @PreAuthorize("authentication.name == #memberDto.userId")   // 해당 userId만 접근 가능
    @PutMapping("/{memberId}")  // memberId로 변경
    public ResponseEntity<MemberDto> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberDto memberDto) {
        MemberDto updatedMember = memberService.updateMember(memberId, memberDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 관리자 회원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")   // memberId로 변경
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    // 회원 프로필 사진 조회
    @GetMapping("/{memberId}/avatar")
    @PreAuthorize("authentication.principal.memberId == #memberId")   // 해당 memberId만 접근 가능
    public ResponseEntity<String> getAvatar(@PathVariable Long memberId) {
        String avatarUrl = memberService.getAvatar(memberId);
        return ResponseEntity.ok(avatarUrl);
    }

    // 회원 프로필 사진 업로드
    @PostMapping("/{memberId}/avatar")
    @PreAuthorize("authentication.principal.memberId == #memberId")   // 해당 memberId만 접근 가능
    public ResponseEntity<ProfileImageUpdateResponse> uploadAvatar(
            @PathVariable Long memberId,
            @RequestParam("avatarImage") MultipartFile avatarImage) {

        // ProfileImageUpdateRequest 객체를 수동으로 생성
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        requestDto.setAvatarImage(avatarImage); // MultipartFile을 요청 DTO에 설정

        // 프로필 사진 업데이트
        ProfileImageUpdateResponse responseDto = memberService.updateAvatar(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}

