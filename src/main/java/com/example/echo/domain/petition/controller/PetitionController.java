package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    // 청원 등록
    @PostMapping
    public ResponseEntity<PetitionResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        return ResponseEntity.ok(petitionService.createPetition(petitionDto));
    }

    // 청원 단건 조회
    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAllPetitions() {
        return ResponseEntity.ok(petitionService.getAllPetitions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> getPetitionById(@PathVariable Long id) {
        return petitionService.getPetitionById(id)
                // 값이 존재할 경우, HTTP 상태 코드: 200 OK를 반환
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // 청원 전체 조회
    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAllPetitions() {
        return ResponseEntity.ok(petitionService.getAllPetitions());
    }

    // 청원 수정
    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long id, @RequestBody PetitionRequestDto petitionDto) {
        try {
            PetitionResponseDto updatedPetition = petitionService.updatePetition(id, petitionDto);
            return ResponseEntity.ok(updatedPetition);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 청원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long id) {
        petitionService.deletePetitionById(id);
        return ResponseEntity.noContent().build();
    }
}
