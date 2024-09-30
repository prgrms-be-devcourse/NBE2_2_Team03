package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.ProfileImageUpdateResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 등록
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberDto memberDto){
        // 기본 아바타 이미지 경로 설정
        memberDto.setAvatarImage("/images/avatar-default.png"); // static 폴더 내 이미지 경로
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.ok(createdMember);
    }

    //id로 회원 조회
    @GetMapping("/{memberId}")  // memberId로 변경
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId){
        MemberDto memberDto = memberService.getMember(memberId);
        return ResponseEntity.ok(memberDto);
    }

    //회원 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers(){
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 수정

    @PutMapping("/{memberId}")  // memberId로 변경
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @RequestBody MemberDto memberDto) {
        MemberDto updatedMember = memberService.updateMember(memberId, memberDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 삭제
    @DeleteMapping("/{memberId}")   // memberId로 변경
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    // UserId로 찾기
    @GetMapping("/{userId}")
    public ResponseEntity<MemberDto> getMemberByUserId(@PathVariable String userId){
        MemberDto memberDto = memberService.findByUserId(userId);
        return ResponseEntity.ok(memberDto);
    }

    // 프로필 사진 조회
    @GetMapping("/{id}/avatar")
    public ResponseEntity<String> getAvatar(@PathVariable Long id) {
        String avatarUrl = memberService.getAvatar(id);
        return ResponseEntity.ok(avatarUrl);
    }

    // 프로필 사진 업로드
    @PostMapping("/{id}/avatar")
    public ResponseEntity<ProfileImageUpdateResponse> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("avatarImage") MultipartFile avatarImage) {

        // ProfileImageUpdateRequest 객체를 수동으로 생성
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        requestDto.setAvatarImage(avatarImage); // MultipartFile을 요청 DTO에 설정

        // 프로필 사진 업데이트
        ProfileImageUpdateResponse responseDto = memberService.updateAvatar(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}

