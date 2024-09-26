package com.example.nbe2_2_team03.service;

import com.example.nbe2_2_team03.dto.request.PetitionRequestDto;
import com.example.nbe2_2_team03.dto.response.PetitionResponseDto;
import com.example.nbe2_2_team03.entity.Petition;
import com.example.nbe2_2_team03.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetitionService {

    @Autowired
    private PetitionRepository petitionRepository;

    public PetitionResponseDto createPetition(PetitionRequestDto petitionDto) {
        Petition petition = Petition.builder()
                .member(petitionDto.getMember())
                .title(petitionDto.getTitle())
                .content(petitionDto.getContent())
                .summary(petitionDto.getSummary())
                .startDate(petitionDto.getStartDate())
                .endDate(petitionDto.getEndDate())
                .category(petitionDto.getCategory())
                .originalUrl(petitionDto.getOriginalUrl())
                .relatedNews(petitionDto.getRelatedNews())
                .likesCount(petitionDto.getLikesCount())
                .interestCount(petitionDto.getInterestCount())
                .agreeCount(petitionDto.getAgreeCount())
                .build();
        return new PetitionResponseDto(petitionRepository.save(petition));
    }

    public List<PetitionResponseDto> getAllPetitions() {
        return petitionRepository.findAll().stream()
                // 스트림의 각 Petition 객체에 대해 PetitionResponseDto 생성자를 호출하여 새로운 PetitionResponseDto 객체를 생성
                .map(PetitionResponseDto::new)
                .collect(Collectors.toList());
    }

    public Optional<PetitionResponseDto> getPetitionById(Long id) {
        return petitionRepository.findById(id)
                .map(PetitionResponseDto::new);
    }

    public PetitionResponseDto updatePetition(Long id, PetitionRequestDto updatedPetitionDto) {
        return petitionRepository.findById(id)
                .map(petition -> { // id로 찾은 값이 존재할 경우 수정
                    Petition updatedPetition = Petition.builder()
                            .petitionId(petition.getPetitionId())
                            .member(updatedPetitionDto.getMember())
                            .title(updatedPetitionDto.getTitle())
                            .content(updatedPetitionDto.getContent())
                            .summary(updatedPetitionDto.getSummary())
                            .startDate(updatedPetitionDto.getStartDate())
                            .endDate(updatedPetitionDto.getEndDate())
                            .category(updatedPetitionDto.getCategory())
                            .originalUrl(updatedPetitionDto.getOriginalUrl())
                            .relatedNews(updatedPetitionDto.getRelatedNews())
                            .likesCount(updatedPetitionDto.getLikesCount())
                            .interestCount(updatedPetitionDto.getInterestCount())
                            .agreeCount(updatedPetitionDto.getAgreeCount())
                            .build();
                    return new PetitionResponseDto(petitionRepository.save(updatedPetition));
                })
                // id로 찾은 값이 존재하지 않을 경우 런타임 예외 처리
                .orElseThrow(() -> new RuntimeException("Petition not found with id: " + id));
    }

    public void deletePetitionById(Long id) {
        petitionRepository.deleteById(id);
    }
}
