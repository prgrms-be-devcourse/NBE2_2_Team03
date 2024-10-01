package com.example.echo.domain.member.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSignUp() {
        // 회원가입 로직 테스트
        Member member = new Member();
        member.setUserId("testuser");
        member.setPassword("testpassword");
        member.setRole(Role.valueOf("USER"));
        member.setEmail("testuser@example.com");
        member.setName("Test User");
        member.setPhone("010-1234-5678");
        member.setAvatarImage("default-avatar.jpg");

        memberService.signUp(member);

        // 회원가입 후 저장된 사용자 정보 검증
        Member savedMember = memberRepository.findByUserId("testuser").get();
        assertThat(savedMember).isNotNull();
        assertThat(passwordEncoder.matches("testpassword", savedMember.getPassword())).isTrue();
        assertThat(savedMember.getEmail()).isEqualTo("testuser@example.com");
        assertThat(savedMember.getName()).isEqualTo("Test User");
        assertThat(savedMember.getPhone()).isEqualTo("010-1234-5678");
}
}