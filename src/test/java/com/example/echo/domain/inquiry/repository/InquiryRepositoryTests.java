package com.example.echo.domain.inquiry.repository;

import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class InquiryRepositoryTests {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("1:1 문의 저장")
    void testSaveInquiry() throws Exception {
        Member member = Member.builder()
                .userId("member2")
                .name("bbb")
                .email("b@b.com")
                .password("111")
                .phone("010-1234-5678")
                .avatarImage("bbb.png")
                .role(Role.USER)
                .build();
        Member savedMember = memberRepository.save(member);
        Inquiry inquiry = Inquiry.builder()
                .member(savedMember)
                .inquiryCategory(InquiryCategory.MEMBER)
                .inquiryTitle("문의제목b")
                .inquiryContent("문의내용b")
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        assertNotNull(savedInquiry);
        System.out.println(savedInquiry);
    }

    @Test
    @DisplayName("1:1 문의 1개 조회")
    void testFindInquiry() throws Exception {
        Long inquiryId = 1L;

        Optional<Inquiry> foundInquiry = inquiryRepository.findById(inquiryId);

        assertNotNull(foundInquiry);
        assertEquals(1L, foundInquiry.get().getInquiryId());
        System.out.println(foundInquiry);
    }

    @Test
    @DisplayName("ADMIN의 모든 1:1 문의 조회 페이징")
    void testFindAllInquiriesAdmin() throws Exception {
        // 1페이지 5사이즈 조회. DB엔 문의 데이터 총 8개 존재.
        Pageable pageable = PageRequest.of(1, 5, Sort.by("inquiryId").descending());

        Page<InquiryResponseDTO> inquiriesAdmin = inquiryRepository.findAllInquiriesAdmin(pageable);

        assertNotNull(inquiriesAdmin);
        assertEquals(8, inquiriesAdmin.getTotalElements());
        assertEquals(2, inquiriesAdmin.getTotalPages());
        assertEquals(1, inquiriesAdmin.getNumber());
        assertEquals(5, inquiriesAdmin.getSize());
        assertEquals(3, inquiriesAdmin.getContent().size());
    }

    @Test
    @DisplayName("USER의 모든 1:1 문의 조회 페이징")
    void testFindAllInquiriesUser() throws Exception {
        // 1페이지 5사이즈 조회. DB엔 memberId = 1로 문의 데이터 총 7개 존재.
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(1, 5, Sort.by("inquiryId").descending());

        Page<InquiryResponseDTO> inquiriesUser = inquiryRepository.findAllInquiriesUser(memberId, pageable);

        assertNotNull(inquiriesUser);
        assertEquals(7, inquiriesUser.getTotalElements());
        assertEquals(2, inquiriesUser.getTotalPages());
        assertEquals(1, inquiriesUser.getNumber());
        assertEquals(5, inquiriesUser.getSize());
        assertEquals(2, inquiriesUser.getContent().size());
    }
}