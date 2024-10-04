package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionDetailResponseDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.exception.MemberNotFoundException;
import com.example.echo.domain.petition.exception.PetitionNotFoundException;
import com.example.echo.domain.petition.repository.PetitionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetitionService {
    private final PetitionRepository petitionRepository;
    private final MemberRepository memberRepository;
    private final SummarizationService summarizationService;

    // 청원 등록
    @Transactional
    public PetitionDetailResponseDto createPetition(PetitionRequestDto petitionDto) {
        // 청원 등록을 위한 관리자 아이디 검색
        Member member = memberRepository.findById(petitionDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(petitionDto.getMemberId()));

        Petition petition = petitionDto.toEntity(member);
        return new PetitionDetailResponseDto(petitionRepository.save(petition));
    }

    // 청원 단건 조회
    @Transactional // 요약 저장하는 경우 있음
    public PetitionDetailResponseDto getPetitionById(Long petitionId) {
        Petition petition = petitionRepository.findById(petitionId).orElseThrow(()
                -> new PetitionNotFoundException(petitionId)); // 청원 번호 조회 없으면 예외
        // 청원 기간 만료 체크 -> 따로 서비스 층에 작성
        //  위에서 exception 발생 안함 = 청원이 존재한다는 뜻 -> 단순 날짜 비교만 진행
        if (isExpired(petition)) { // 만료되었으면 예외 발생
            throw new PetitionNotFoundException(petitionId);
        }

        String summary = petition.getSummary(); // 요약 내용 체크

        if (summary != null) {
            // 내용 요약 있으면 바로 반환
            return new PetitionDetailResponseDto(petition);

        } else {
            // 내용 요약 없으면 요약 진행 및 저장 후 반환
            String content = petition.getContent(); // 원본 내용
            // 줄바꿈 문단 처리 방식 -> 공백 전부 제거 or 줄바꿈은 유지
            String originText = content.replaceAll("\\s+", " ");
            String summaryText = summarizationService.getSummarizedText(originText); // 요약 결과
            // null -> 요약된 내용으로 변경
            petition.changeSummary(summaryText);
            petitionRepository.save(petition);
            return new PetitionDetailResponseDto(petition);
        }
    }

    // 청원 전체 조회
    public Page<PetitionResponseDto> getPetitions(Pageable pageable) {
        return petitionRepository.findAll(pageable).map(PetitionResponseDto::new);
    }

    // 청원 전체 조회 (카테고리별)
    public Page<PetitionResponseDto> getPetitionsByCategory(Pageable pageable, Category category) {
        return petitionRepository.findByCategory(pageable, category).map(PetitionResponseDto::new);
    }

    // 청원 만료일 순 5개 조회
    public List<PetitionResponseDto> getEndDatePetitions() {
        Pageable pageable = PageRequest.of(0, 5);
        return petitionRepository.getEndDatePetitions(pageable);
    }

    // 청원 동의자 순 5개 조회
    public List<PetitionResponseDto> getLikesCountPetitions() {
        Pageable pageable = PageRequest.of(0, 5);
        return petitionRepository.getLikesCountPetitions(pageable);
    }

    // 청원 카테고리 선택 5개 조회 (랜덤 순)
    public List<PetitionResponseDto> getRandomCategoryPetitions(Category category) {
        Pageable pageable = PageRequest.of(0, 5);
        return petitionRepository.getCategoryPetitionsInRandomOrder(category, pageable);
    }

    // 청원 수정
    @Transactional
    public PetitionDetailResponseDto updatePetition(Long petitionId, PetitionRequestDto updatedPetitionDto) {
        Petition existingPetition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));

        Member member = memberRepository.findById(updatedPetitionDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(updatedPetitionDto.getMemberId()));

        Petition updatedPetition = updatedPetitionDto.toEntityWithExistingData(existingPetition, member);
        return new PetitionDetailResponseDto(petitionRepository.save(updatedPetition));
    }

    // 청원 삭제
    @Transactional
    public void deletePetitionById(Long petitionId) {
        if (!petitionRepository.existsById(petitionId)) {
            throw new PetitionNotFoundException(petitionId);
        }
        petitionRepository.deleteById(petitionId);
    }

    // 좋아요 기능
    @Transactional
    public ResponseEntity<String> toggleLikeOnPetition(Long petitionId, Long memberId) {
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("비회원은 좋아요 기능을 사용할 수 없습니다.");
        }

        // 멤버 존재 여부 확인
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }

        // 청원 조회
        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));

        // 좋아요를 추가하거나 제거
        boolean isLiked = petition.toggleLike(memberId);

        // 변경 사항을 저장
        petitionRepository.save(petition);

        // 좋아요가 추가되었는지 제거되었는지에 따라 적절한 메시지 반환
        String message = isLiked ? "좋아요가 추가되었습니다." : "좋아요가 제거되었습니다.";
        return ResponseEntity.ok(message);
    }

    // 청원 좋아요 수 기준으로 5개 조회
    public List<PetitionResponseDto> getPetitionsByLikeCount() {
        Pageable pageable = PageRequest.of(0, 5); // 0페이지에서 5개 항목 조회
        return petitionRepository.getPetitionsOrderByLikesCount(pageable);
    }
}

    // 청원 기간 체크
    public boolean isExpired(Petition petition) { // petition or petitionId
        // 오늘 날짜와 비교
        // 만료 전이면 false
        if (petition.getEndDate() == null ){
            throw new IllegalArgumentException("만료일은 null 일 수 없습니다.");
        }
        // 현재 형식이 LocalDateTime 만료일 00:00:00
        // 청원 만료일 오후 3시 일 경우 만료 전이나 이미 만료됐다고 판단
        // 만료일 + 1 을 기준으로 체크
        return petition.getEndDate().plusDays(1).isBefore(LocalDateTime.now()); // 만료일이 지난 경우 true
    }
}
