package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.crawling.PetitionCrawl;
import com.example.echo.domain.petition.service.PetitionCrawlService;
import com.example.echo.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Petition Crawling Controller", description = "청원 크롤링 API")
public class PetitionCrawlController {

    private final PetitionCrawlService petitionCrawlService;

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    // 상세페이지 이동 가능한 리스트 크롤링
    @Operation(summary = "청원 홈페이지 크롤링", description = "청원 상세페이지로 이동할 수 있는 리스트를 크롤링합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/petitioncrawl/{id}")
    public ResponseEntity<ApiResponse<List<PetitionCrawl>>> crawlDesPage(
            @Parameter(description = "크롤링할 관리자 회원의 ID") @PathVariable Long id) {
        String url = "https://petitions.assembly.go.kr/proceed/onGoingAll";
        List<PetitionCrawl> crawledData = petitionCrawlService.dynamicCrawl(id, url);
        return ResponseEntity.ok(ApiResponse.success(crawledData));
    }
}
