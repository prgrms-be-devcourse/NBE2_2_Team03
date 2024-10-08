package com.example.echo.domain.member.service;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberLoginRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.global.exception.ErrorCode;
import com.example.echo.global.exception.PetitionCustomException;
import com.example.echo.global.security.util.JWTUtil;
import com.example.echo.global.util.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final UploadUtil uploadUtil;
    private final JWTUtil jwtUtil;

    // 회원 로그인
    public Map<String, String> login(MemberLoginRequest memberRequest) {
        Member member = findMemberByUserId(memberRequest.getUserId());
        validatePassword(memberRequest.getPassword(), member.getPassword());
        return makeToken(member);
    }

    // 회원 등록
    @Transactional
    public MemberResponse createMember(MemberCreateRequest memberRequest) {
        Member member = memberRequest.toMember();
        member.setPassword(passwordEncoder.encode(member.getPassword()));   // password 암호화
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    // 회원 조회
    public MemberResponse getMember(Long memberId) {
        return MemberResponse.from(findMemberById(memberId));
    }

    // 전체 회원 조회
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    // 회원 정보 수정
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberRequest) {
        Member member = findMemberById(memberId);

        // 이메일 중복 확인 (이메일이 변경되었을 때만 확인)
        if (!member.getEmail().equals(memberRequest.getEmail())) {
            checkEmailDuplicate(memberRequest.getEmail());
        }

        // 전화번호 중복 확인 (전화번호가 변경되었을 때만 확인)
        if (!member.getPhone().equals(memberRequest.getPhone())) {
            checkPhoneDuplicate(memberRequest.getPhone());
        }

        // MemberUpdateRequest를 사용하여 업데이트
        memberRequest.updateMember(member);

        return MemberResponse.from(memberRepository.save(member)); // 수정된 회원 정보 저장
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.delete(findMemberById(memberId));
    }

    // 프로필 사진 조회
    public String getAvatar(Long memberId) {
        return findMemberById(memberId).getAvatarImage();
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

    // userID로 회원 조회
    private Member findMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new PetitionCustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // password 검증
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PetitionCustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    // 공통 메서드: 회원 ID로 회원 조회
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new PetitionCustomException(ErrorCode.MEMBER_NOT_FOUND));
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
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new PetitionCustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    // 전화번호 중복 확인
    private void checkPhoneDuplicate(String phone) {
        if (memberRepository.findByPhone(phone).isPresent()) {
            throw new PetitionCustomException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    // 보호된 데이터 요청 시 사용자 정보 조회
    public MemberResponse getMemberInfo(Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자 ID 가져오기
        Member member = findMemberByUserId(userId); // 사용자 정보를 DB에서 조회
        return MemberResponse.from(member); // MemberResponse로 변환하여 반환
    }
}
