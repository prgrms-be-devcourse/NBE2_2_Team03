package com.example.echo.domain.petition.crawling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PetitionCrawl {
    // 크롤링해서 얻은 값을 이용해서 재크롤링하여 얻어온 값을 저장하기 위해 생성한 class

    // 청원 제목
    private String title;
    // 동의 시작일 종료일
    // 크롤링만 한 경우 기간으로 값 받는다.
    // (service 에서 시작일, 종료일 나눠 db에 저장)
    private String period;

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