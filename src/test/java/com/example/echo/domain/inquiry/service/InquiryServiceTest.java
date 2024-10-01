package com.example.echo.domain.inquiry.service;

import com.example.echo.domain.inquiry.dto.request.InquiryPageRequestDTO;
import com.example.echo.domain.inquiry.dto.request.InquiryRequestDTO;
import com.example.echo.domain.inquiry.dto.response.InquiryResponseDTO;
import com.example.echo.domain.inquiry.entity.InquiryCategory;
import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InquiryServiceTest {

    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("1:1 문의 생성")
    void testCreateInquiry() {
        InquiryRequestDTO inquiryRequestDTO = InquiryRequestDTO.builder()
                .memberId(1L)
                .inquiryCategory(InquiryCategory.MEMBER)
                .inquiryTitle("문의제목11")
                .inquiryContent("문의내용11")
                .build();

        InquiryResponseDTO createdInquiry = inquiryService.createInquiry(inquiryRequestDTO);

        assertNotNull(createdInquiry);
        System.out.println(createdInquiry);
    }

    @Test
    @DisplayName("모든 회원 1:1 문의 상세 조회")
    void testGetInquiryById() {
        Long inquiryId = 1L;

        // 정상 조회
        InquiryResponseDTO foundInquiry = inquiryService.getInquiryById(inquiryId);
        assertNotNull(foundInquiry);

        // 예외 발생 테스트
        assertEquals("1:1 문의 정보를 찾을 수 없습니다.",
                assertThrows(RuntimeException.class, () ->
                        inquiryService.getInquiryById(100L)
                ).getMessage()
        );
    }

    @Test
    @DisplayName("ADMIN 1:1 문의 전체 조회")
    void testFindAllForAdmin() throws Exception {
        // 2페이지 5사이즈 조회. DB엔 문의 데이터 총 8개 존재.
        Member member = Member.builder()
                .userId("member4")
                .name("ddd")
                .email("d@d.com")
                .password("111")
                .phone("010-1234-5678")
                .avatarImage("ddd.png")
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);
        InquiryPageRequestDTO inquiryPageRequestDTO = InquiryPageRequestDTO.builder().pageNumber(2).build();

        Page<InquiryResponseDTO> inquiriesAdmin =
                inquiryService.getInquiriesByMemberRole(member.getMemberId(), inquiryPageRequestDTO);

        assertNotNull(inquiriesAdmin);
        assertEquals(8, inquiriesAdmin.getTotalElements());
        assertEquals(2, inquiriesAdmin.getTotalPages());
        assertEquals(1, inquiriesAdmin.getNumber());
        assertEquals(5, inquiriesAdmin.getSize());
        assertEquals(3, inquiriesAdmin.getContent().size());
    }

    @Test
    @DisplayName("USER 등록한 1:1 문의 전체 조회")
    void testFindAllForUser() throws Exception {
        // 2페이지 5사이즈 조회. DB엔 memberId = 1로 문의 데이터 총 7개 존재.
        Long memberId = 1L;
        InquiryPageRequestDTO inquiryPageRequestDTO = InquiryPageRequestDTO.builder().pageNumber(2).build();

        Page<InquiryResponseDTO> inquiriesUser =
                inquiryService.getInquiriesByMemberRole(memberId, inquiryPageRequestDTO);

        assertNotNull(inquiriesUser);
        assertEquals(7, inquiriesUser.getTotalElements());
        assertEquals(2, inquiriesUser.getTotalPages());
        assertEquals(1, inquiriesUser.getNumber());
        assertEquals(5, inquiriesUser.getSize());
        assertEquals(2, inquiriesUser.getContent().size());
    }
}