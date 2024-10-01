package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member Controller", description = "회원 관리 API") // 태그 추가
public class MemberController {

    private final MemberService memberService;

    //회원 등록
    @Operation(summary = "회원 등록", description = " 신규 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberDto memberDto){
        // 기본 아바타 이미지 경로 설정
        memberDto.setAvatarImage("/images/avatar-default.png"); // static 폴더 내 이미지 경로
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.ok(createdMember);
    }

    //id로 회원 조회
    @Operation(summary = "회원 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.")
    })
    @GetMapping("/{memberId}")  // memberId로 변경
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId){
        MemberDto memberDto = memberService.getMember(memberId);
        return ResponseEntity.ok(memberDto);
    }

    //회원 전체 조회
    @Operation(summary = "전체 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers(){
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 수정
    @Operation(summary = "회원 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.")
    })
    @PutMapping("/{memberId}")  // memberId로 변경
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long memberId, @RequestBody MemberDto memberDto) {
        MemberDto updatedMember = memberService.updateMember(memberId, memberDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제", description = "회원 ID로 회원을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.")
    })
    @DeleteMapping("/{memberId}")   // memberId로 변경
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    // 프로필 사진 조회
    @Operation(summary = "프로필 사진 조회", description = "회원 ID로 프로필 사진 URL을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 사진 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.")
    })
    @GetMapping("/{id}/avatar")
    public ResponseEntity<String> getAvatar(@PathVariable Long id) {
        String avatarUrl = memberService.getAvatar(id);
        return ResponseEntity.ok(avatarUrl);
    }

    // 프로필 사진 업로드
    @Operation(summary = "프로필 사진 업로드", description = "회원 ID로 프로필 사진을 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 사진 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않습니다.")
    })
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

