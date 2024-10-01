package com.example.echo.domain.member.service;

import com.example.echo.domain.member.dto.MemberDto;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.ProfileImageUpdateResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.global.security.util.JWTUtil;
import com.example.echo.global.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final UploadUtil uploadUtil;
    private final JWTUtil jwtUtil;

    // 로그인 로직
    public Map<String, String> login(MemberDto memberDto) {
        // 1. 사용자 정보 조회
        Optional<Member> memberOpt = memberRepository.findByUserId(memberDto.getUserId());
        if (memberOpt.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Member member = memberOpt.get();

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성
        return makeToken(member);
    }

    //로그인 패스워드 암호화하여 매칭시키기
    public Member signUp(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    //로그인할때 아이디로 찾는 로직
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Member member = memberOptional.get();

        // UserDetails 반환 (username, password, 권한)
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }


    // 회원 등록
    @Transactional
    public MemberDto createMember(MemberDto memberDto) {
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword())); // 패스워드 암호화
        Member savedMember = memberRepository.save(memberDto.toEntity());
        return MemberDto.of(savedMember);
    }

    //회원 조회
    public MemberDto getMember(Long memberId) {  // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원정보를 찾을수 없습니다."));
        return MemberDto.of(member);
    }

    //전체 회원 조회
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }

    //회원 정보 수정
    @Transactional
    public MemberDto updateMember(Long memberId, MemberDto memberDto) { // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));

        // 이메일 중복 확인
        if (!member.getEmail().equals(memberDto.getEmail())) {
            checkEmailDuplicate(memberDto.getEmail());
        }

        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setAvatarImage(memberDto.getAvatarImage());
        member.setRole(memberDto.getRole());

        return MemberDto.of(memberRepository.save(member));
    }

    //id로 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) { // Long memberId로 변경
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }

    // 프로필 사진 조회
    public String getAvatar(Long memberId) {
        Member member = findMemberById(memberId);
        return member.getAvatarImage(); // URL 반환
    }

    // 프로필 사진 업데이트
    @Transactional
    public ProfileImageUpdateResponse updateAvatar(Long id, ProfileImageUpdateRequest requestDto) {
        Member member = findMemberById(id);

        // 파일 업로드 후 경로 받기
        String avatarUrl = uploadUtil.upload(requestDto.getAvatarImage());
        member.setAvatarImage(avatarUrl); // 경로 업데이트

        // 회원 정보를 저장하고 응답 DTO 생성
        Member updatedMember = memberRepository.save(member);
        return ProfileImageUpdateResponse.from(updatedMember);
    }

    // 공통 메서드: 회원 ID로 회원 조회
    public Member findMemberById(Long memberId) {   // InquiryService에서도 회원 객체를 직접 접근해야 해서 public 변경
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원정보를 찾을 수 없습니다."));
    }

    // JWT 토큰 생성
    private Map<String, String> makeToken(Member member) {
        Map<String, Object> payloadMap = member.getPayload();
        String accessToken = jwtUtil.createToken(payloadMap, 60); // 60분 유효
        String refreshToken = jwtUtil.createToken(Map.of("userId", member.getUserId()), 60 * 24 * 7); // 7일 유효
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);
    }

    // 이메일 중복 확인
    private void checkEmailDuplicate(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new RuntimeException("이미 존재하는 이메일입니다.");
                });
    }
}
