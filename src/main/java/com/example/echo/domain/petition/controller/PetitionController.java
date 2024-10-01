package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.service.PetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
@Tag(name = "Petition Controller", description = "청원 관리 API") // Swagger 태그 추가
public class PetitionController {

    private final PetitionService petitionService;

    // 청원 등록
    @Operation(summary = "청원 등록", description = "새로운 청원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "청원 등록 성공"),  // 성공 응답
            @ApiResponse(responseCode = "400", description = "청원 등록 실패"),  // 실패 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")        // 서버 오류 응답
    })
    @PostMapping // @Valid 추가하여 유효성 검사
    public ResponseEntity<PetitionResponseDto> createPetition(@Valid @RequestBody PetitionRequestDto petitionDto) {
        return ResponseEntity.ok(petitionService.createPetition(petitionDto));
    }

    // 청원 단건 조회
    @Operation(summary = "청원 단건 조회", description = "특정 ID의 청원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "청원 조회 성공"),    // 성공 응답
            @ApiResponse(responseCode = "404", description = "청원 찾을 수 없음"), // 실패 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")          // 서버 오류 응답
    })
    @GetMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> getPetitionById(@PathVariable Long id) {
        return petitionService.getPetitionById(id)
                // 값이 존재할 경우, HTTP 상태 코드: 200 OK를 반환
                .map(ResponseEntity::ok)
                // 값이 없을 경우, 404 NOT FOUND 반환
                .orElse(ResponseEntity.notFound().build());
    }

    // 청원 전체 조회
    @Operation(summary = "청원 전체 조회", description = "모든 청원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "청원 목록 조회 성공"), // 성공 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")            // 서버 오류 응답
    })
    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAllPetitions() {
        return ResponseEntity.ok(petitionService.getAllPetitions());
    }

    // 청원 수정
    @Operation(summary = "청원 수정", description = "특정 ID의 청원을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "청원 수정 성공"),    // 성공 응답
            @ApiResponse(responseCode = "404", description = "청원 찾을 수 없음"), // 실패 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")          // 서버 오류 응답
    })
    @PutMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> updatePetition(@PathVariable Long id, @Valid @RequestBody PetitionRequestDto petitionDto) {
            PetitionResponseDto updatedPetition = petitionService.updatePetition(id, petitionDto);
            return ResponseEntity.ok(updatedPetition);
    }

    // 청원 삭제
    @Operation(summary = "청원 삭제", description = "특정 ID의 청원을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "청원 삭제 성공"),    // 성공 응답
            @ApiResponse(responseCode = "404", description = "청원 찾을 수 없음"), // 실패 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")          // 서버 오류 응답
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetitionById(@PathVariable Long id) {
        petitionService.deletePetitionById(id);
        return ResponseEntity.noContent().build();
    }
}