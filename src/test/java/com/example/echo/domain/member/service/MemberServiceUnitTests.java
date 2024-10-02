package com.example.echo.domain.member.service;

import com.example.echo.domain.member.dto.request.MemberCreateRequest;
import com.example.echo.domain.member.dto.request.MemberUpdateRequest;
import com.example.echo.domain.member.dto.request.ProfileImageUpdateRequest;
import com.example.echo.domain.member.dto.response.MemberResponse;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.global.exception.MemberNotFoundException;
import com.example.echo.global.util.UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceUnitTests {

    @Mock
    private MemberRepository memberRepository; // MemberRepository 모의 객체

    @Mock
    private UploadUtil uploadUtil; // UploadUtil 모의 객체

    @Mock
    private PasswordEncoder passwordEncoder; // PasswordEncoder 모의 객체

    @InjectMocks
    private MemberService memberService; // 테스트 할 MemberService 객체

    private Member member; // 테스트용 Member 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        member = new Member();
        member.setMemberId(1L);
        member.setUserId("testUser");
        member.setName("테스트 사용자");
        member.setEmail("test@example.com");
        member.setRole(Role.USER); // 기본 역할 설정
    }

    // 회원 등록 테스트
    @Test
    void testCreateMember() {
        // 주어진 데이터
        MemberCreateRequest request = MemberCreateRequest.builder()
                .userId("testUser")
                .name("테스트 사용자")
                .email("test@example.com")
                .password("password")
                .phone("010-1234-5678")
                .avatarImage(null)
                .role(Role.USER)
                .build();

        // 회원 등록 메서드 호출
        when(memberRepository.save(any(Member.class))).thenReturn(member); // 모의 저장 동작 설정

        MemberResponse response = memberService.createMember(request); // 메서드 실행

        // 검증
        assertNotNull(response);
        assertEquals("testUser", response.getUserId()); // 사용자 ID 검증
        verify(memberRepository).save(any(Member.class)); // save 메서드 호출 검증
    }

    // 회원 조회 테스트
    @Test
    void testGetMember() {
        // 모의 객체 반환 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMember(1L); // 메서드 실행

        // 검증
        assertNotNull(response);
        assertEquals("테스트 사용자", response.getName()); // 이름 검증
        verify(memberRepository).findById(1L); // findById 호출 검증
    }

    // 회원 업데이트 테스트
    @Test
    void testUpdateMember() {
        // 모의 객체 반환 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member); // 모의 저장 동작 설정

        MemberUpdateRequest updateRequest = MemberUpdateRequest.builder()
                .userId("testUser")
                .name("업데이트된 사용자")
                .email("update@example.com")
                .phone("010-9876-5432")
                .avatarImage(null)
                .role(Role.USER)
                .build();

        MemberResponse response = memberService.updateMember(1L, updateRequest); // 메서드 실행

        // 검증
        assertNotNull(response);
        assertEquals("업데이트된 사용자", response.getName()); // 이름 검증
        verify(memberRepository).save(any(Member.class)); // save 호출 검증
    }

    // 회원 삭제 테스트
    @Test
    void testDeleteMember() {
        // 모의 객체 반환 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L); // 메서드 실행

        verify(memberRepository).delete(member); // delete 호출 검증
    }

    // 회원을 찾을 수 없는 경우 예외 테스트
    @Test
    void testGetMemberNotFound() {
        // 예외가 발생할 것으로 예상
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // 예외 발생 검증
        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(1L));
    }

    // 프로필 사진 업데이트 테스트
    // 프로필 사진 업데이트 테스트
    @Test
    void testUpdateAvatar() {
        // 주어진 데이터
        ProfileImageUpdateRequest requestDto = new ProfileImageUpdateRequest();
        MultipartFile mockFile = mock(MultipartFile.class); // 모의 MultipartFile 객체
        when(mockFile.getContentType()).thenReturn("image/png"); // 파일 타입 설정
        when(mockFile.getOriginalFilename()).thenReturn("test-avatar.png"); // 파일 이름 설정
        requestDto.setAvatarImage(mockFile);

        // 모의 객체 반환 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(uploadUtil.upload(mockFile)).thenReturn("uploaded/path/test-avatar.png"); // 업로드 경로 설정

        MemberResponse response = memberService.updateAvatar(1L, requestDto); // 메서드 실행

        // 검증
        assertNotNull(response);
        assertEquals("uploaded/path/test-avatar.png", response.getAvatarImage()); // 업로드된 프로필 이미지 경로 검증
        assertEquals("uploaded/path/test-avatar.png", member.getAvatarImage()); // Member의 avatarImage 검증
        verify(memberRepository).save(member); // save 호출 검증
    }


}
