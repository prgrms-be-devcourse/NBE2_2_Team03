package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionDetailResponseDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PetitionServiceTest {

    @Autowired
    private MockMvc mockMvc;

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
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void createPetition() {
        // given
        PetitionRequestDto request = createPetitionRequest(testMember.getMemberId());

        // when
        PetitionDetailResponseDto response = petitionService.createPetition(request);

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
        PetitionDetailResponseDto response = petitionService.getPetitionById(petition.getPetitionId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 청원");
    }

    @Test
    @DisplayName("청원 전체 조회 - 좋아요 순 정렬")
    void getPetitions_SortByLikes() throws Exception {
        // given
        createMultiplePetitions(5);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "agreeCount"));

        // when
        Page<PetitionResponseDto> petitionsPage = petitionService.getPetitions(pageable);

        // then
        List<PetitionResponseDto> petitions = petitionsPage.getContent();
        assertThat(petitions).hasSize(5);
        assertThat(petitions).isSortedAccordingTo((p1, p2) -> p2.getAgreeCount().compareTo(p1.getAgreeCount()));

        // API 호출 테스트
        mockMvc.perform(get("/api/petitions")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "agreeCount,desc"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("청원 전체 조회 - 만료일 순 정렬")
    void getPetitions_SortByExpirationDate() throws Exception {
        // given
        createMultiplePetitions(5);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "endDate"));

        // when
        Page<PetitionResponseDto> petitionsPage = petitionService.getPetitions(pageable);

        // then
        List<PetitionResponseDto> petitions = petitionsPage.getContent();
        assertThat(petitions).hasSize(5);
        assertThat(petitions).isSortedAccordingTo((p1, p2) -> p2.getEndDate().compareTo(p1.getEndDate()));

        // API 호출 테스트
        mockMvc.perform(get("/api/petitions")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "endDate,desc"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("청원 카테고리별 조회")
    void getPetitions_FilterByCategory() throws Exception {
        // given
        createMultiplePetitions(10);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PetitionResponseDto> petitionsPage = petitionService.getPetitionsByCategory(Category.EDUCATION, pageable);

        // then
        List<PetitionResponseDto> petitions = petitionsPage.getContent();
        assertThat(petitions).allMatch(petition -> petition.getCategory() == Category.EDUCATION);

        // API 호출 테스트
        mockMvc.perform(get("/api/petitions/category/EDUCATION")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("청원 수정")
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void updatePetition() {
        // given
        Petition petition = createPetition(testMember);
        LocalDateTime newStartDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newEndDate = LocalDateTime.now().plusDays(31);

        PetitionRequestDto updateRequest = PetitionRequestDto.builder()
                .memberId(testMember.getMemberId())
                .title("수정된 청원 제목")
                .content("수정된 청원 내용")
                .summary("수정된 청원 요약")
                .startDate(newStartDate)
                .endDate(newEndDate)
                .category(Category.EDUCATION)
                .originalUrl("http://updated-test.com")
                .relatedNews("수정된 관련 뉴스")
                .build();

        // when
        PetitionDetailResponseDto updatedPetition = petitionService.updatePetition(petition.getPetitionId(), updateRequest);

        // then
        assertThat(updatedPetition.getTitle()).isEqualTo("수정된 청원 제목");
        assertThat(updatedPetition.getContent()).isEqualTo("수정된 청원 내용");
        assertThat(updatedPetition.getSummary()).isEqualTo("수정된 청원 요약");
        assertThat(updatedPetition.getStartDate()).isEqualToIgnoringNanos(newStartDate);
        assertThat(updatedPetition.getEndDate()).isEqualToIgnoringNanos(newEndDate);
        assertThat(updatedPetition.getCategory()).isEqualTo(Category.EDUCATION);
        assertThat(updatedPetition.getOriginalUrl()).isEqualTo("http://updated-test.com");
        assertThat(updatedPetition.getRelatedNews()).isEqualTo("수정된 관련 뉴스");
    }

    @Test
    @DisplayName("청원 삭제")
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
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
                .userId("testAdmin")
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
                .content("테스트 청원 내용")
                .summary("테스트 청원 요약")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .category(Category.POLITICS)
                .originalUrl("http://test.com")
                .relatedNews("테스트 관련 뉴스")
                .agreeCount((int) (Math.random() * 1000))
                .build();
        return petitionRepository.save(petition);
    }

    /**
     * [페이징 테스트용 다수 청원 엔티티 생성]
     * 정렬 테스트를 위해 각각 다른 값 부여
     */
    private void createMultiplePetitions(int count) {
        for (int i = 0; i < count; i++) {
            Petition petition = Petition.builder()
                    .member(testMember)
                    .title("테스트 청원 " + i)
                    .content("테스트 청원 내용 " + i)
                    .summary("테스트 청원 요약 " + i)
                    .startDate(LocalDateTime.now().plusDays(i))
                    .endDate(LocalDateTime.now().plusDays(30 + i))
                    .category(i % 2 == 0 ? Category.POLITICS : Category.EDUCATION)
                    .originalUrl("http://test" + i + ".com")
                    .relatedNews("테스트 관련 뉴스 " + i)
                    .agreeCount((int) (Math.random() * 1000))
                    .build();
            petitionRepository.save(petition);
        }
    }
}
