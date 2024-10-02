package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PagingRequestDto;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.service.PetitionService;
import com.example.echo.domain.petition.service.SummarizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
@Tag(name = "Petition Controller", description = "청원 관리 API")
public class PetitionController {

    private final PetitionService petitionService;
    private final SummarizationService summarizationService;

    // 청원 등록
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 등록", description = "새로운 청원을 등록합니다.")
    @PostMapping
    public ResponseEntity<PetitionDetailResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        PetitionDetailResponseDto createdPetition = petitionService.createPetition(petitionDto);
        return ResponseEntity.ok(createdPetition);
    }

    // 청원 단건 조회
    @Operation(summary = "청원 단건 조회", description = "특정 ID의 청원을 조회합니다.")
    @GetMapping("/{petitionId}")
    public ResponseEntity<PetitionDetailResponseDto> getPetitionById(@PathVariable Long petitionId) {
        PetitionDetailResponseDto petition = petitionService.getPetitionById(petitionId);
        return ResponseEntity.ok(petition);
    }

    // 청원 전체 조회
    @Operation(summary = "청원 전체 조회", description = "모든 청원을 페이지별로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<PetitionResponseDto>> getPetitions(Pageable pageable) {
        Page<PetitionResponseDto> petitions = petitionService.getPetitions(pageable);
        return ResponseEntity.ok(petitions);
    }

    // 청원 만료일 순 5개 조회
    @Operation(summary = "청원 만료일 기준 조회", description = "만료일이 가까운 청원 5개를 조회합니다.")
    @GetMapping("/view/endDate")
    public ResponseEntity<List<PetitionResponseDto>> getEndDatePetitions() {
        List<PetitionResponseDto> endDatePetitions = petitionService.getEndDatePetitions();
        return ResponseEntity.ok(endDatePetitions);
    }

    // 청원 동의자 순 5개 조회
    @Operation(summary = "청원 동의자 수 기준 조회", description = "동의자 수 많은 청원 5개를 조회합니다.")
    @GetMapping("/view/agreeCount")
    public ResponseEntity<List<PetitionResponseDto>> getAgreeCountPetitions() {
        List<PetitionResponseDto> agreeCountPetitions = petitionService.getAgreeCountPetitions();
        return ResponseEntity.ok(agreeCountPetitions);
    }

    // 청원 카테고리 선택 5개 조회
    @Operation(summary = "청원 카테고리별 조회", description = "특정 카테고리의 청원 5개를 랜덤 순으로 조회합니다.")
    @GetMapping("/view/category/{category}")
    public ResponseEntity<List<PetitionResponseDto>> getRandomCategoryPetitions(@PathVariable Category category) {
        List<PetitionResponseDto> categoryPetitions = petitionService.getRandomCategoryPetitions(category);
        return ResponseEntity.ok(categoryPetitions);
    }


    // 청원 수정
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 수정", description = "특정 ID의 청원을 수정합니다.")
    @PutMapping("/{petitionId}")
    public ResponseEntity<PetitionDetailResponseDto> updatePetition(@PathVariable Long petitionId, @RequestBody PetitionRequestDto petitionDto) {
        PetitionDetailResponseDto updatedPetition = petitionService.updatePetition(petitionId, petitionDto);
        return ResponseEntity.ok(updatedPetition);
    }

    // 청원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 삭제", description = "특정 ID의 청원을 삭제합니다.")
    @DeleteMapping("/{petitionId}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long petitionId) {
        petitionService.deletePetitionById(petitionId);
        return ResponseEntity.noContent().build();
    }
}
