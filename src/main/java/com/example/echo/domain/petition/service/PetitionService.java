package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetitionService {

    @Autowired
    private PetitionRepository petitionRepository;

    @Autowired
    private MemberRepository memberRepository;

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
                .likesCount(0)
                .interestCount(0)
                .agreeCount(0)
                .build();
        return new PetitionResponseDto(petitionRepository.save(petition));
    }

    public List<PetitionResponseDto> getAllPetitions() {
        return petitionRepository.findAll().stream()
                .map(PetitionResponseDto::new)
                .collect(Collectors.toList());
    }

    public Optional<PetitionResponseDto> getPetitionById(Long id) {
        return petitionRepository.findById(id)
                .map(PetitionResponseDto::new);
    }

    @Transactional
    public PetitionResponseDto updatePetition(Long id, PetitionRequestDto updatedPetitionDto) {
        return petitionRepository.findById(id)
                .map(petition -> { // Petition 값이 존재할 경우 함수 실행, 값이 없다면 런타임 예외처리
                    Member member = memberRepository.findById(updatedPetitionDto.getMemberId())
                            .orElseThrow(() -> new RuntimeException("Member not found"));

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
                .orElseThrow(() -> new RuntimeException("Petition not found with id: " + id));
    }

    @Transactional
    public void deletePetitionById(Long id) {
        petitionRepository.deleteById(id);
    }
}