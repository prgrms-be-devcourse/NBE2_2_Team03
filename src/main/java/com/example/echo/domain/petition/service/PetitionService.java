package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.exception.MemberNotFoundException;
import com.example.echo.domain.petition.exception.PetitionNotFoundException;
import com.example.echo.domain.petition.repository.PetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new MemberNotFoundException(petitionDto.getMemberId()));

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
    public PetitionResponseDto getPetitionById(Long petitionId) {
        return petitionRepository.findById(petitionId)
                .map(PetitionResponseDto::new)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));
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
        Petition existingPetition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new PetitionNotFoundException(petitionId));

        Member member = memberRepository.findById(updatedPetitionDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(updatedPetitionDto.getMemberId()));

        Petition updatedPetition = Petition.builder()
                .petitionId(existingPetition.getPetitionId()) // 기존 ID 유지
                .member(member)
                .title(updatedPetitionDto.getTitle())
                .content(updatedPetitionDto.getContent())
                .summary(updatedPetitionDto.getSummary())
                .startDate(updatedPetitionDto.getStartDate())
                .endDate(updatedPetitionDto.getEndDate())
                .category(updatedPetitionDto.getCategory())
                .originalUrl(updatedPetitionDto.getOriginalUrl())
                .relatedNews(updatedPetitionDto.getRelatedNews())
                .likesCount(existingPetition.getLikesCount())  // 기존 값 유지
                .interestCount(existingPetition.getInterestCount())  // 기존 값 유지
                .agreeCount(existingPetition.getAgreeCount())  // 기존 값 유지
                .build();

        Petition savedPetition = petitionRepository.save(updatedPetition);
        return new PetitionResponseDto(savedPetition);
    }

    // 청원 삭제
    @Transactional
    public void deletePetitionById(Long petitionId) {
        if (!petitionRepository.existsById(petitionId)) {
            throw new PetitionNotFoundException(petitionId);
        }
        petitionRepository.deleteById(petitionId);
    }
}