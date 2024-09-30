package com.example.echo.domain.member.service;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.global.exception.MemberNotFoundException;
import com.example.echo.global.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final UploadUtil uploadUtil;

    // 회원 등록
    @Transactional
    public MemberResponse createMember(MemberCreateRequest memberRequest) {
        Member member = memberRequest.toMember(); // DTO를 Member로 변환
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    //회원 조회
    public MemberResponse getMember(Long memberId) {
        return MemberResponse.from(findMemberById(memberId));
    }

    //전체 회원 조회
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    //회원 정보 수정
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberRequest) {
        Member member = findMemberById(memberId);
        memberRequest.updateMember(member); // DTO로 Member 업데이트
        return MemberResponse.from(memberRepository.save(member));
    }


    //id로 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.delete(findMemberById(memberId));
    }

    // 프로필 사진 조회
    public String getAvatar(Long id) {
        return findMemberById(id).getAvatarImage();
    }

    // 프로필 사진 업데이트
    @Transactional
    public MemberResponse updateAvatar(Long id, ProfileImageUpdateRequest requestDto) {
        Member member = findMemberById(id);
        String avatarUrl = uploadUtil.upload(requestDto.getAvatarImage());
        member.setAvatarImage(avatarUrl);
        memberRepository.save(member);

        return MemberResponse.from(member); // MemberResponse로 반환
    }


    // 공통 메서드: 회원 ID로 회원 조회
    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원정보를 찾을 수 없습니다."));
    }
}
