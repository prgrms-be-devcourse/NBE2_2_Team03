package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.service.PetitionCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetitionCrawlController {
    @Autowired
    private PetitionCrawlService petitionCrawlService;

    //상세페이지 이동 가능한 리스트 크롤링
    @GetMapping("/petitioncrawl")
    public String crawlDesPage() { //url 입력
        String url = "https://petitions.assembly.go.kr/proceed/onGoingAll";
        return petitionCrawlService.dynamicCrawl(url);
    }
}
