package com.example.echo.domain.member.service;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원등록
    @Transactional
    public MemberDto createMember(MemberDto memberDto){
        Member savedMember = memberRepository.save(memberDto.toEntity());
        return MemberDto.of(savedMember);
    }

    //회원 조회
    public MemberDto getMember(Long memberId){  // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("회원정보를 찾을수 없습니다."));
        return MemberDto.of(member);
    }

    //전체 회원 조회
    public List<MemberDto> getAllMembers(){
        return memberRepository.findAll().stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }

    //회원 정보 수정
    @Transactional
    public MemberDto updateMember(Long memberId, MemberDto memberDto){ // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
            .orElseThrow(()-> new RuntimeException("회원정보를 찾을 수 없습니다."));

        member.setUserId(memberDto.getUserId()); // userId로 변경
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setAvatarImage(memberDto.getAvatarImage());
        member.setRole(memberDto.getRole());

        return MemberDto.of(memberRepository.save(member));
    }

    //id로 회원 삭제
    @Transactional
    public void deleteMember(Long memberId){ // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }
}
