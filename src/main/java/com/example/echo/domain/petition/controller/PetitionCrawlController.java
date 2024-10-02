package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.service.PetitionCrawlService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetitionCrawlController {
    @Autowired
    private PetitionCrawlService petitionCrawlService;

    //상세페이지 이동 가능한 리스트 크롤링
    @Operation(summary = "상세페이지 이동 가능한 리스트 크롤링", description = "청원 상세페이지로 이동할 수 있는 리스트를 크롤링합니다.")
    @GetMapping("/petitioncrawl/{id}") // 관리자 정보 들어가게 하기 위해 로그인 처리?
    public String crawlDesPage(@PathVariable Long id) { //url 입력
        String url = "https://petitions.assembly.go.kr/proceed/onGoingAll";
        return petitionCrawlService.dynamicCrawl(id, url);
    }
}
