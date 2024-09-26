package com.example.nbe2_2_team03.controller;

import com.example.nbe2_2_team03.dto.request.PetitionRequestDto;
import com.example.nbe2_2_team03.dto.response.PetitionResponseDto;
import com.example.nbe2_2_team03.entity.Petition;
import com.example.nbe2_2_team03.service.PetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petitions")
public class PetitionController {

    @Autowired
    private PetitionService petitionService;

    @PostMapping
    public ResponseEntity<PetitionResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        return ResponseEntity.ok(petitionService.createPetition(petitionDto));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long id, @RequestBody PetitionRequestDto petitionDto) {
        try {
            PetitionResponseDto updatedPetition = petitionService.updatePetition(id, petitionDto);
            return ResponseEntity.ok(updatedPetition);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long id) {
        petitionService.deletePetitionById(id);
        return ResponseEntity.noContent().build();
    }
}
