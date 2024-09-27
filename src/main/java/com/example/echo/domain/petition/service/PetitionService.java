package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Petition petition = Petition.builder()
                .member(member)
                .title(petitionDto.getTitle())
                .content(petitionDto.getContent())
                .summary(petitionDto.getSummary())
                .startDate(petitionDto.getStartDate())
                .endDate(petitionDto.getEndDate())
                .category(petitionDto.getCategory())
                .originalUrl(petitionDto.getOriginalUrl())
                .relatedNews(petitionDto.getRelatedNews())
                .build();
        return new PetitionResponseDto(petitionRepository.save(petition));
    }

    // 청원 단건 조회
    public Optional<PetitionResponseDto> getPetitionById(Long petitionId) {
        return petitionRepository.findById(petitionId)
                .map(PetitionResponseDto::new);
    }

    // 청원 전체 조회
    public List<PetitionResponseDto> getAllPetitions() {
        return petitionRepository.findAll().stream()
                // 각 Petition 객체에 대해 new PetitionResponseDto(petition)을 수행
                .map(PetitionResponseDto::new)
                // 변환된 PetitionResponseDto 객체들을 리스트로 모아서 반환
                .collect(Collectors.toList());
    }

    // 청원 수정
    @Transactional
    public PetitionResponseDto updatePetition(Long petitionId, PetitionRequestDto updatedPetitionDto) {
        // petitionId로 청원을 조회하고, 존재하지 않으면 예외를 던짐
        return petitionRepository.findById(petitionId)
                .map(petition -> {
                    // 청원을 작성한 회원(관리자)를 조회하고, 없으면 예외를 던짐
                    Member member = memberRepository.findById(updatedPetitionDto.getMemberId())
                            .orElseThrow(() -> new RuntimeException("Member not found"));

                    /**
                     * Petition 엔티티에서 Setter를 사용한다면 코드 간소화 가능
                     * 현재 코드의 경우 청원 생성 날짜와 같은 필드가 엔티티에 추가된다면
                     * 업데이트하지 않아야 할 필드도 덮어쓸 수 있는 가능성 있음
                     */
                    Petition updatedPetition = Petition.builder()
                            .petitionId(petition.getPetitionId())
                            .member(member)
                            .title(updatedPetitionDto.getTitle())
                            .content(updatedPetitionDto.getContent())
                            .summary(updatedPetitionDto.getSummary())
                            .startDate(updatedPetitionDto.getStartDate())
                            .endDate(updatedPetitionDto.getEndDate())
                            .category(updatedPetitionDto.getCategory())
                            .originalUrl(updatedPetitionDto.getOriginalUrl())
                            .relatedNews(updatedPetitionDto.getRelatedNews())
                            .likesCount(petition.getLikesCount())
                            .interestCount(petition.getInterestCount())
                            .agreeCount(petition.getAgreeCount())
                            .build();

                    return new PetitionResponseDto(petitionRepository.save(updatedPetition));
                })
                .orElseThrow(() -> new RuntimeException("Petition not found with id: " + petitionId));
    }

    // 청원 삭제
    @Transactional
    public void deletePetitionById(Long petitionId) {
        petitionRepository.deleteById(petitionId);
    }
}