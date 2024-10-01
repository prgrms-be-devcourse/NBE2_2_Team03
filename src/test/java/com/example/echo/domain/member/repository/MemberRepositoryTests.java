package com.example.echo.domain.member.repository;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // H2 데이터베이스 사용
@Transactional
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        testMember = Member.builder()
                .userId("testUser")
                .name("테스트 사용자")
                .email("test@example.com")
                .password("password123")
                .phone("010-1234-5678")
                .avatarImage("/images/avatar-default.png")
                .role(Role.USER)
                .build();
    }

    @Test
    @Rollback
    public void testCreateMember() {
        Member savedMember = memberRepository.save(testMember);
        Assertions.assertNotNull(savedMember.getMemberId());
        Assertions.assertEquals("testUser", savedMember.getUserId());
    }

    @Test
    @Rollback
    public void testFindMemberById() {
        Member savedMember = memberRepository.save(testMember);

        Optional<Member> foundMember = memberRepository.findById(savedMember.getMemberId());
        Assertions.assertTrue(foundMember.isPresent());
        Assertions.assertEquals(savedMember.getUserId(), foundMember.get().getUserId());
    }

    @Test
    @Rollback
    public void testDeleteMember() {
        Member savedMember = memberRepository.save(testMember);
        memberRepository.delete(savedMember);

        Optional<Member> foundMember = memberRepository.findById(savedMember.getMemberId());
        Assertions.assertFalse(foundMember.isPresent());
    }

    @Test
    @Rollback
    public void testUpdateAvatarImage() {
        Member savedMember = memberRepository.save(testMember);

        // 프로필 이미지 업데이트
        savedMember.setAvatarImage("newAvatar.png");
        memberRepository.save(savedMember);

        Optional<Member> updatedMember = memberRepository.findById(savedMember.getMemberId());
        Assertions.assertTrue(updatedMember.isPresent());
        Assertions.assertEquals("newAvatar.png", updatedMember.get().getAvatarImage());
    }
}
