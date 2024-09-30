package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import com.example.echo.domain.petition.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;
    private final PetitionRepository petitionRepository;

    // 청원 등록
    @PostMapping
    public ResponseEntity<PetitionResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto createdPetition = petitionService.createPetition(petitionDto);
        return ResponseEntity.ok(createdPetition);
    }

    // 청원 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> getPetitionById(@PathVariable Long id) {
        PetitionResponseDto petition = petitionService.getPetitionById(id);
        return ResponseEntity.ok(petition);
    }

    // 청원 전체 조회
    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAllPetitions() {
        List<PetitionResponseDto> petitions = petitionService.getAllPetitions();
        return ResponseEntity.ok(petitions);
    }

    // 청원 전체 조회 (페이징 기능 사용)
    @GetMapping
    public Page<PetitionResponseDto> getPetitionsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "agreeCount") String sortBy,
            // 동의자 순으로 나열
            @RequestParam(defaultValue = "desc") String direction) {

        // Sort.Direction으로 asc 또는 desc로 정렬 방식 설정
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        // sortBy 문자열은 실제 엔티티의 필드 이름을 참조: "agreeCount"는 Petition 엔티티의 필드 이름과 일치
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return petitionService.getPetitionsByPage(pageable);
    }

    // 청원 수정
    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long id, @RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto updatedPetition = petitionService.updatePetition(id, petitionDto);
        return ResponseEntity.ok(updatedPetition);
    }

    // 청원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long id) {
        petitionService.deletePetitionById(id);
        return ResponseEntity.noContent().build();
    }

}
