package com.example.echo.domain.petition.crawling;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PetitionCrawl {

    @Schema(description = "청원 제목", example = "독도의 날(10월 25일), 국가기념일 지정에 관한 청원")
    private String title;

    @Schema(description = "동의 시작일과 종료일 (형식: yyyy-MM-dd)", example = "2024-09-11 ~ 2024-10-11")
    private String period;

    @Schema(description = "청원 분류", example = "외교/통일/국방/안보")
    private String category;

    @Schema(description = "동의자 수 (형식: 1,000명)", example = "20,527명")
    private String agreeCount;

    @Schema(description = "청원 원본 주소", example = "https://petitions.assembly.go.kr/proceed/onGoingAll/20D5FC4DDB8625D7E064B49691C6967B")
    private String href;

    @Schema(description = "청원 상세 내용", example = "1. 독도가 위험해지고 있습니다...")
    private String content;

    public void changeContent(String content) {
        this.content = content;
    }
}
