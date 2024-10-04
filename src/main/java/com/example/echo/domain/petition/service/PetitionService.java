package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.exception.MemberNotFoundException;
import com.example.echo.domain.petition.exception.PetitionNotFoundException;
import com.example.echo.domain.petition.repository.PetitionRepository;
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

    // 청원 등록
    @Transactional
    public PetitionResponseDto createPetition(PetitionRequestDto petitionDto) {
        // 청원 등록을 위한 관리자 아이디 검색
        Member member = memberRepository.findById(petitionDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(petitionDto.getMemberId()));

        Petition petition = petitionDto.toEntity(member);
        return new PetitionResponseDto(petitionRepository.save(petition));
    }

    // 청원 단건 조회
    public PetitionResponseDto getPetitionById(Long petitionId) {
        return petitionRepository.findById(petitionId)
                .map(PetitionResponseDto::new)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));
    }

    // 청원 전체 조회
    public Page<PetitionResponseDto> getPetitions(Pageable pageable) {
        return petitionRepository.findAll(pageable).map(PetitionResponseDto::new);
    }

    // 청원 만료일 순 5개 조회
    public List<PetitionResponseDto> getEndDatePetitions() {
        Pageable pageable = PageRequest.of(0, 5);
        return petitionRepository.getEndDatePetitions(pageable);
    }

    // 청원 동의자 순 5개 조회
    public List<PetitionResponseDto> getAgreeCountPetitions() {
        Pageable pageable = PageRequest.of(0, 5);
        return petitionRepository.getAgreeCountPetitions(pageable);
    }

    // 청원 수정
    @Transactional
    public PetitionResponseDto updatePetition(Long petitionId, PetitionRequestDto updatedPetitionDto) {
        Petition existingPetition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));

        Member member = memberRepository.findById(updatedPetitionDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(updatedPetitionDto.getMemberId()));

        Petition updatedPetition = updatedPetitionDto.toEntityWithExistingData(existingPetition, member);
        return new PetitionResponseDto(petitionRepository.save(updatedPetition));
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

