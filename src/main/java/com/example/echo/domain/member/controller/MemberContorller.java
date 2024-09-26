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
public class MemberContorller {

    private final MemberService memberService;

    //회원 등록
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto){
        MemberDto createdMember = memberService.createMember(memberDto);
        return ResponseEntity.ok(createdMember);
    }

    //id로 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id){
        MemberDto memberDto = memberService.getMember(id);
        return ResponseEntity.ok(memberDto);
    }

    //회원 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers(){
        List<MemberDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto memberDto) {
        MemberDto updatedMember = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}

