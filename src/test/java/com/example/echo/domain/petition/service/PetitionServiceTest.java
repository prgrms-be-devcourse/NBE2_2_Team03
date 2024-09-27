package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PetitionServiceTest {

    @Autowired
    private PetitionService petitionService;

    @Autowired
    private PetitionRepository petitionRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        petitionRepository.deleteAll();
        memberRepository.deleteAll();
        testMember = createMember();
    }

    @Test
    @DisplayName("청원 생성")
    void createPetition() {
        // given
        PetitionRequestDto request = createPetitionRequest(testMember.getMemberId());

        // when
        PetitionResponseDto response = petitionService.createPetition(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 청원");
        assertThat(petitionRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("청원 단건 조회")
    void getPetitionById() {
        // given
        Petition petition = createPetition(testMember);

        // when
        PetitionResponseDto response = petitionService.getPetitionById(petition.getPetitionId()).orElse(null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 청원");
    }

    @Test
    @DisplayName("청원 전체 조회")
    void getAllPetitions() {
        // given
        createPetition(testMember);
        createPetition(testMember);

        // when
        List<PetitionResponseDto> petitions = petitionService.getAllPetitions();

        // then
        assertThat(petitions).hasSize(2);
    }

    @Test
    @DisplayName("청원 수정")
    void updatePetition() {
        // given
        Petition petition = createPetition(testMember);
        PetitionRequestDto updateRequest = createPetitionRequest(testMember.getMemberId());
        updateRequest = PetitionRequestDto.builder()
                .memberId(updateRequest.getMemberId())
                .title("수정된 청원")
                .content(updateRequest.getContent())
                .summary(updateRequest.getSummary())
                .startDate(updateRequest.getStartDate())
                .endDate(updateRequest.getEndDate())
                .category(updateRequest.getCategory())
                .originalUrl(updateRequest.getOriginalUrl())
                .relatedNews(updateRequest.getRelatedNews())
                .build();

        // when
        PetitionResponseDto updatedPetition = petitionService.updatePetition(petition.getPetitionId(), updateRequest);

        // then
        assertThat(updatedPetition.getTitle()).isEqualTo("수정된 청원");
    }

    @Test
    @DisplayName("청원 삭제")
    void deletePetition() {
        // given
        Petition petition = createPetition(testMember);

        // when
        petitionService.deletePetitionById(petition.getPetitionId());

        // then
        assertThat(petitionRepository.count()).isEqualTo(0);
    }

    /**
     * [테스트용 관리자 회원 생성]
     * 이 메소드는 청원 관련 테스트에 필요한 관리자 권한을 가진 회원을 생성
     */
    private Member createMember() {
        Member member = Member.builder()
                .userId("userid")
                .name("김관리")
                .email("test@example.com")
                .password("password123")
                .phone("010-1234-5678")
                .avatarImage("default.jpg")
                .role(Role.ADMIN)
                .build();
        return memberRepository.save(member);
    }

    /**
     * [청원 등록 요청을 위한 DTO 생성]
     * 청원 등록 시 관리자가 입력하는 요청 데이터를 모방하여 PetitionRequestDto 생성
     */
    private PetitionRequestDto createPetitionRequest(Long memberId) {
        return PetitionRequestDto.builder()
                .memberId(memberId)
                .title("테스트 청원")
                .content("테스트 내용")
                .summary("테스트 요약")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .category(Category.POLITICS)
                .originalUrl("http://test.com")
                .relatedNews("테스트 뉴스")
                .build();
    }

    /**
     * [테스트용 청원 엔티티 생성]
     * 이 메소드는 청원 조회/수정/삭제 테스트에 필요한 청원 데이터를 데이터베이스에 저장
     */
    private Petition createPetition(Member member) {
        Petition petition = Petition.builder()
                .member(member)
                .title("테스트 청원")
                .content("테스트 내용")
                .summary("테스트 요약")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .category(Category.POLITICS)
                .originalUrl("http://test.com")
                .relatedNews("테스트 뉴스")
                .build();
        return petitionRepository.save(petition);
    }
}