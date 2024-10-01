package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PagingRequestDto;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    // 청원 등록
    @PostMapping
    public ResponseEntity<PetitionResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto createdPetition = petitionService.createPetition(petitionDto);
        return ResponseEntity.ok(createdPetition);
    }

    // 청원 단건 조회
    @GetMapping("/{petitionId}")
    public ResponseEntity<PetitionResponseDto> getPetitionById(@PathVariable Long petitionId) {
        PetitionResponseDto petition = petitionService.getPetitionById(petitionId);
        return ResponseEntity.ok(petition);
    }

    // 청원 전체 조회
    @GetMapping
    public ResponseEntity<Page<PetitionResponseDto>> getPetitions(PagingRequestDto pagingRequestDto) {
        Page<PetitionResponseDto> petitions = petitionService.getPetitions(pagingRequestDto.toPageable());
        return ResponseEntity.ok(petitions);
    }

    // 청원 수정
    @PutMapping("/{petitionId}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long petitionId, @RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto updatedPetition = petitionService.updatePetition(petitionId, petitionDto);
        return ResponseEntity.ok(updatedPetition);
    }

    // 청원 삭제
    @DeleteMapping("/{petitionId}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long petitionId) {
        petitionService.deletePetitionById(petitionId);
        return ResponseEntity.noContent().build();
    }
}
