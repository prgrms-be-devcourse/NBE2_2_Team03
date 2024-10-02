package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.entity.crawling.PetitionCrawl;
import com.example.echo.domain.petition.service.PetitionCrawlService;
import com.example.echo.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
public class PetitionCrawlController {

    private final PetitionCrawlService petitionCrawlService;

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    //상세페이지 이동 가능한 리스트 크롤링
    @Operation(summary = "상세페이지 이동 가능한 리스트 크롤링", description = "청원 상세페이지로 이동할 수 있는 리스트를 크롤링합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/petitioncrawl/{id}") // 관리자 정보 들어가게 하기 위해 로그인 처리?
    public ResponseEntity<ApiResponse<List<PetitionCrawl>>> crawlDesPage(@PathVariable Long id) { //url 입력
        String url = "https://petitions.assembly.go.kr/proceed/onGoingAll";
        List<PetitionCrawl> crawledData = petitionCrawlService.dynamicCrawl(id, url);
        return ResponseEntity.ok(ApiResponse.success(crawledData));
    }
}
