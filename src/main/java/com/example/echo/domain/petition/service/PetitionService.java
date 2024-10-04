package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.InterestRequestDTO;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.InterestPetitionResponseDTO;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.exception.MemberNotFoundException;
import com.example.echo.domain.petition.exception.PetitionNotFoundException;
import com.example.echo.domain.petition.repository.PetitionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<PetitionResponseDto> getPetitions(Pageable pageable, Category category) {
        if (category != null) {
            return petitionRepository.findByCategory(category, pageable).map(PetitionResponseDto::new);
        } else {
            return petitionRepository.findAll(pageable).map(PetitionResponseDto::new);
        }
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


    // 관심목록 추가 & count+1
    @Transactional
    public void addInterest(InterestRequestDTO interestRequestDTO) {
        Petition petition = petitionRepository.findById(interestRequestDTO.getPetitionId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 청원을 찾을 수 없습니다."));
        Member member = memberRepository.findById(interestRequestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 회원을 찾을 수 없습니다."));

        if (!member.getInterestList().contains(interestRequestDTO.getPetitionId())) {
            petition.setInterestCount(petition.getInterestCount() + 1);
            member.getInterestList().add(interestRequestDTO.getPetitionId());

            petitionRepository.save(petition);
            memberRepository.save(member);
        }
    }

    //관심목록 제거 & count-1
    @Transactional
    public void removeInterest(InterestRequestDTO interestRequestDTO) {
        Petition petition = petitionRepository.findById(interestRequestDTO.getPetitionId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 청원을 찾을 수 없습니다."));
        Member member = memberRepository.findById(interestRequestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 회원을 찾을 수 없습니다."));

        if (member.getInterestList().contains(interestRequestDTO.getPetitionId())) {
            petition.setInterestCount(petition.getInterestCount() - 1);
            member.getInterestList().remove(interestRequestDTO.getPetitionId());

            petitionRepository.save(petition);
            memberRepository.save(member);
        }
    }

    //관심 목록 조회
    @Transactional(readOnly = true)
    public List<InterestPetitionResponseDTO> getInterestList(Member member) {
        List<Long> interestList = member.getInterestList();

        return interestList.stream()
                .map(petitionId -> {
                    Petition petition = petitionRepository.findById(petitionId)
                            .orElseThrow(() -> new EntityNotFoundException("해당하는 청원ID를 찾을 수 없습니다 : " + petitionId));
                    return new InterestPetitionResponseDTO(petition);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterestPetitionResponseDTO> getPetitionsByInterestCount() {
        List<Petition> petitions = petitionRepository.findAll(Sort.by(Sort.Direction.DESC, "interestCount"));

        return petitions.stream()
            .map(petition -> new InterestPetitionResponseDTO(petition))  // DTO로 변환
            .collect(Collectors.toList());
    }

}