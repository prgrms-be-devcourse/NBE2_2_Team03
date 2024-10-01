package com.example.echo.domain.petition.dto.request;

import com.example.echo.domain.petition.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "청원 등록 시 요청하는 데이터")
public class PetitionRequestDto {

    @NotNull(message = "회원 번호는 필수입니다.")  // memberId는 필수 입력
    @Schema(description = "회원 번호", required = true)
    private Long memberId;  // Member 엔티티 대신 ID만 받음

    @NotBlank(message = "제목은 필수입니다.")  // 제목 필수 입력
    @Schema(description = "청원 제목", required = true)
    private String title;

    @NotBlank(message = "내용은 필수입니다.")  // 내용 필수 입력
    @Schema(description = "청원 내용", required = true)
    private String content;

    @Schema(description = "청원 요약")
    private String summary;

    @NotNull(message = "시작 날짜는 필수입니다.")
    @Schema(description = "청원 시작 날짜", required = true)
    private LocalDateTime startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    @Schema(description = "청원 종료 날짜", required = true)
    private LocalDateTime endDate;

    @NotNull(message = "카테고리는 필수입니다.")  // 카테고리 필수
    @Schema(description = "청원 카테고리", required = true)
    private Category category;

    @NotBlank(message = "원본 URL은 필수입니다.")  // 원본 URL 필수 입력
    @Pattern(regexp = "^(https?://).+", message = "유효한 URL 형식이 아닙니다.") // 형식 검증
    @Schema(description = "원본 URL", required = true)
    private String originalUrl;

    @NotBlank(message = "관련 뉴스는 필수입니다.")  // 관련 뉴스 필수 입력
    @Schema(description = "관련 뉴스", required = true)
    private String relatedNews;
    // likesCount, interestCount, agreeCount는 제거 (서버에서 관리)
}