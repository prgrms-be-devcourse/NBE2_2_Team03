package com.example.echo.domain.petition.controller;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.petition.dto.request.InterestRequestDTO;
import com.example.echo.domain.petition.dto.request.PagingRequestDto;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.InterestPetitionResponseDTO;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.service.PetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
@Tag(name = "Petition Controller", description = "청원 관리 API")
public class PetitionController {

    private final PetitionService petitionService;

    // 청원 등록
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 등록", description = "새로운 청원을 등록합니다.")
    @PostMapping
    public ResponseEntity<PetitionResponseDto> createPetition(@RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto createdPetition = petitionService.createPetition(petitionDto);
        return ResponseEntity.ok(createdPetition);
    }

    // 청원 단건 조회
    @PreAuthorize("permitAll()")
    @Operation(summary = "청원 단건 조회", description = "특정 ID의 청원을 조회합니다.")
    @GetMapping("/{petitionId}")
    public ResponseEntity<PetitionResponseDto> getPetitionById(@PathVariable Long petitionId) {
        PetitionResponseDto petition = petitionService.getPetitionById(petitionId);
        return ResponseEntity.ok(petition);
    }

    // 청원 전체 조회
    @PreAuthorize("permitAll()")
    @Operation(summary = "청원 전체 조회", description = "모든 청원을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<PetitionResponseDto>> getPetitions(PagingRequestDto pagingRequestDto) {
        Page<PetitionResponseDto> petitions = petitionService.getPetitions(pagingRequestDto.toPageable(), pagingRequestDto.getCategory());
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

    // 청원 수정
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 수정", description = "특정 ID의 청원을 수정합니다.")
    @PutMapping("/{petitionId}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long petitionId, @RequestBody PetitionRequestDto petitionDto) {
        PetitionResponseDto updatedPetition = petitionService.updatePetition(petitionId, petitionDto);
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/interestAdd")
    public ResponseEntity<?> addInterestt(@RequestBody InterestRequestDTO requestDTO) {
        try {
            petitionService.addInterest(requestDTO);
            return ResponseEntity.ok("추가되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("관심사 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/interestRemove")
    public ResponseEntity<?> removeInterest(@RequestBody InterestRequestDTO requestDTO) {
        try {
            petitionService.removeInterest(requestDTO);
            return ResponseEntity.ok("관심사가 성공적으로 제거되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("관심사 제거 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')" )
    @GetMapping("/Myinterest")
    public ResponseEntity<?> getInterestList(@AuthenticationPrincipal Member member) {
        try {
            // 회원의 관심 목록 조회
            List<InterestPetitionResponseDTO> interestList = petitionService.getInterestList(member);
            return ResponseEntity.ok(interestList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("관심 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    @GetMapping("/interests")
    public ResponseEntity<?> getPetitionsByInterestCount() {
        try {
            List<InterestPetitionResponseDTO> petitionList = petitionService.getPetitionsByInterestCount();
            return ResponseEntity.ok(petitionList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("관심사 순위 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
