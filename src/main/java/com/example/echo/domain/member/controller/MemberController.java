package com.example.echo.domain.member.controller;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 등록
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto){
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
}

