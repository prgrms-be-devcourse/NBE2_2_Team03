package com.example.echo.domain.petition.controller;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
import com.example.echo.domain.petition.dto.request.InterestRequestDTO;
import com.example.echo.domain.petition.dto.request.PetitionRequestDto;
import com.example.echo.domain.petition.dto.response.InterestPetitionResponseDTO;
import com.example.echo.domain.petition.dto.response.PetitionDetailResponseDto;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.service.PetitionService;
import com.example.echo.global.api.ApiResponse;
import com.example.echo.global.exception.ErrorCode;
import com.example.echo.global.exception.PetitionCustomException;
import com.example.echo.global.security.auth.CustomUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final MemberRepository memberRepository;

    // 청원 등록
    @Operation(summary = "청원 등록", description = "새로운 청원을 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PetitionDetailResponseDto>> createPetition(
            @Parameter(description = "청원 등록 요청 정보", required = true) @RequestBody PetitionRequestDto petitionDto) {
        PetitionDetailResponseDto createdPetition = petitionService.createPetition(petitionDto);
        return ResponseEntity.ok(ApiResponse.success(createdPetition));
    }

    // 청원 단건 조회
    @Operation(summary = "청원 단건 조회", description = "특정 ID의 청원을 조회합니다.")
    @GetMapping("/{petitionId}")
    public ResponseEntity<ApiResponse<PetitionDetailResponseDto>> getPetitionById(
            @Parameter(description = "조회할 청원의 ID", required = true) @PathVariable Long petitionId) {
        PetitionDetailResponseDto petition = petitionService.getPetitionById(petitionId);
        return ResponseEntity.ok(ApiResponse.success(petition));
    }

    // 청원 전체 조회
    @Operation(summary = "청원 전체 조회", description = "모든 청원을 페이지별로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PetitionResponseDto>>> getPetitions(
            @Parameter(description = "청원 조회 페이징 요청 정보", required = true) Pageable pageable) {
        Page<PetitionResponseDto> petitions = petitionService.getOngoingPetitions(pageable);
        return ResponseEntity.ok(ApiResponse.success(petitions));
    }

    // 청원 카테고리별 조회
    @Operation(summary = "카테고리별 청원 조회", description = "특정 카테고리의 모든 청원을 페이지별로 조회합니다.")
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<PetitionResponseDto>>> getPetitionsByCategory(
            @Parameter(description = "조회할 청원의 카테고리", required = true) @PathVariable Category category,
            @Parameter(description = "청원 조회 페이징 요청 정보", required = true) Pageable pageable) {
        Page<PetitionResponseDto> petitions = petitionService.getPetitionsByCategory(pageable, category);
        return ResponseEntity.ok(ApiResponse.success(petitions));
    }

    // 청원 만료일 순 5개 조회
    @Operation(summary = "청원 만료일 기준 조회", description = "만료일이 가까운 청원 5개를 조회합니다.")
    @GetMapping("/view/endDate")
    public ResponseEntity<ApiResponse<List<PetitionResponseDto>>> getEndDatePetitions() {
        List<PetitionResponseDto> endDatePetitions = petitionService.getEndDatePetitions();
        return ResponseEntity.ok(ApiResponse.success(endDatePetitions));
    }

    // 청원 좋아요 순 5개 조회
    @Operation(summary = "청원 좋아요 수 기준 조회", description = "좋아요 수가 많은 청원 5개를 조회합니다.")
    @GetMapping("/view/likesCount")
    public ResponseEntity<ApiResponse<List<PetitionResponseDto>>> getLikesCountPetitions() {
        List<PetitionResponseDto> likesCountPetitions = petitionService.getLikesCountPetitions();
        return ResponseEntity.ok(ApiResponse.success(likesCountPetitions));
    }

    // 청원 좋아요 기능
    @PreAuthorize("authentication.principal.memberId == #memberId")
    @Operation(summary = "청원 좋아요 토글", description = "청원에 좋아요를 추가하거나 제거합니다.")
    @PostMapping("/{petitionId}/like")
    public ResponseEntity<ApiResponse<String>> toggleLike(
            @Parameter(description = "좋아요를 추가하거나 제거할 청원의 ID", required = true) @PathVariable Long petitionId,
            @Parameter(description = "좋아요를 클릭한 회원의 ID", required = true) @RequestParam(required = false) Long memberId) {
        String message = petitionService.toggleLikeOnPetition(petitionId, memberId);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    // 청원 카테고리 선택 5개 조회
    @Operation(summary = "청원 카테고리별 조회", description = "특정 카테고리의 청원 5개를 랜덤으로 조회합니다.")
    @GetMapping("/view/category/{category}")
    public ResponseEntity<ApiResponse<List<PetitionResponseDto>>> getRandomCategoryPetitions(
            @Parameter(description = "랜덤으로 조회할 청원의 카테고리", required = true) @PathVariable Category category) {
        List<PetitionResponseDto> categoryPetitions = petitionService.getRandomCategoryPetitions(category);
        return ResponseEntity.ok(ApiResponse.success(categoryPetitions));
    }

    // 제목으로 청원 검색
    @Operation(summary = "청원 제목으로 검색", description = "제목에 검색어가 포함된 청원을 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PetitionDetailResponseDto>>> searchPetitions(
            @Parameter(description = "검색할 제목의 키워드", required = true) @RequestParam String query) {
        List<PetitionDetailResponseDto> petitions = petitionService.searchPetitionsByTitle(query);
        return ResponseEntity.ok(ApiResponse.success(petitions));
    }

    // 청원 수정
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 수정", description = "특정 ID의 청원을 수정합니다.")
    @PutMapping("/{petitionId}")
    public ResponseEntity<ApiResponse<PetitionDetailResponseDto>> updatePetition(
            @Parameter(description = "수정할 청원의 ID", required = true) @PathVariable Long petitionId,
            @Parameter(description = "청원 수정 요청 정보", required = true) @RequestBody PetitionRequestDto petitionDto) {
        PetitionDetailResponseDto updatedPetition = petitionService.updatePetition(petitionId, petitionDto);
        return ResponseEntity.ok(ApiResponse.success(updatedPetition));
    }

    // 청원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "청원 삭제", description = "특정 ID의 청원을 삭제합니다.")
    @DeleteMapping("/{petitionId}")
    public ResponseEntity<ApiResponse<Void>> deletePetitionById(
            @Parameter(description = "삭제할 청원의 ID", required = true) @PathVariable Long petitionId) {
        petitionService.deletePetitionById(petitionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null));
    }

    // 관심 목록 추가
    @PreAuthorize("authentication.principal.memberId == #requestDTO.memberId")
    @Operation(summary = "관심 목록 추가", description = "청원을 관심 목록에 추가합니다.")
    @PostMapping("/interestAdd")
    public ResponseEntity<ApiResponse<?>> addInterest(
            @Parameter(description = "관심 목록 추가 요청 정보", required = true) @RequestBody InterestRequestDTO requestDTO) {
        try {
            petitionService.addInterest(requestDTO);
            return ResponseEntity.ok(ApiResponse.success("추가되었습니다.", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Entity not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관심사 추가 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 관심 목록 제거
    @PreAuthorize("authentication.principal.memberId == #requestDTO.memberId")
    @Operation(summary = "관심 목록 제거", description = "청원을 관심 목록에서 제거합니다.")
    @PostMapping("/interestRemove")
    public ResponseEntity<ApiResponse<?>> removeInterest(
            @Parameter(description = "관심 목록 추가 요청 정보", required = true) @RequestBody InterestRequestDTO requestDTO) {
        try {
            petitionService.removeInterest(requestDTO);
            return ResponseEntity.ok(ApiResponse.success("관심사가 성공적으로 제거되었습니다.", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Entity not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관심사 제거 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 나의 관심 목록 조회
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내 관심 목록 조회", description = "현재 사용자의 관심 목록을 조회합니다.")
    @GetMapping("/Myinterest")
    public ResponseEntity<ApiResponse<?>> getInterestList(
            @Parameter(description = "현재 인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserPrincipal principal) {
        Member member = memberRepository.findById(principal.getMemberId())
                .orElseThrow(() -> new PetitionCustomException(ErrorCode.MEMBER_NOT_FOUND));
        try {
            // 회원의 관심 목록 조회
            List<InterestPetitionResponseDTO> interestList = petitionService.getInterestList(member);
            return ResponseEntity.ok(ApiResponse.success(interestList));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관심 목록 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 관심 목록 수에 따라 정렬
    @Operation(summary = "관심 목록 수 기준 조회", description = "관심 목록 수에 따라 청원을 정렬하여 조회합니다.")
    @GetMapping("/interests")
    public ResponseEntity<ApiResponse<?>> getPetitionsByInterestCount() {
        try {
            List<InterestPetitionResponseDTO> petitionList = petitionService.getPetitionsByInterestCount();
            return ResponseEntity.ok(ApiResponse.success(petitionList));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("관심사 순위 목록 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
