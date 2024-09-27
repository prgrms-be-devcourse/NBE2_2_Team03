package com.example.echo.domain.petition.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetitionCrawl {
    // 청원 제목
    private String title;
    // 동의 시작일 종료일
    private String startDate;

    // 분류
    private String category;

    //동의자 수 // ~ 명 이 붙어서 우선 string 으로 받음
    private String agreeCount;

    // 원본 주소
    private String href;

    // 원본 내용 청원 상세 내용
    private String content;

    public void changeContent(String content) {this.content = content; }

}