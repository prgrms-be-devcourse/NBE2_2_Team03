package com.example.echo.domain.petition.controller;

import com.example.echo.domain.petition.dto.request.PetitionCrawlRequestDto;
import com.example.echo.domain.petition.dto.response.PetitionCrawlResponseDto;
import com.example.echo.domain.petition.service.PetitionCrawlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@Tag(name = "Petition Crawl Controller", description = "청원 데이터 크롤링 관련 API") // 태그 추가
public class PetitionCrawlController {
    @Autowired
    private PetitionCrawlService petitionCrawlService;

    //상세페이지 이동 가능한 리스트 크롤링
    @Operation(summary = "상세페이지 이동 가능한 리스트 크롤링", description = "청원 상세페이지로 이동할 수 있는 리스트를 크롤링합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "크롤링 성공"),     // 성공 응답
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),     // 실패 응답
            @ApiResponse(responseCode = "500", description = "서버 오류")        // 서버 오류
    })
    @PostMapping("/petitioncrawl")
    public ResponseEntity<?> crawlDesPage(@Valid @RequestBody PetitionCrawlRequestDto petitionCrawlRequestDto, BindingResult result) {

        // 유효성 검증 오류 처리
        if (result.hasErrors()) {
            // 검증 오류가 있을 경우, 첫 번째 오류 메시지를 반환
            String errorMessage = result.getFieldError().getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        String url = petitionCrawlRequestDto.getUrl(); // Dto에서 URL을 가져옴

        // URL 유효성 검증
        if (!isValidUrl(url)) {
            // 유효하지 않은 URL일 경우, BAD_REQUEST 응답을 반환
            return ResponseEntity.badRequest().body("유효한 URL을 입력해 주세요.");
        }



        try {
//             URL이 특정 값을 가지면 강제로 Exception을 발생시킴 서버 오류 테스트 시 사용
//            if (url.equals("http://force500error.com")) {
//                throw new RuntimeException("강제로 서버 오류 발생");
//            }

            // 크롤링 서비스 호출
            String crawlResult = petitionCrawlService.dynamicCrawl(url);
            return ResponseEntity.ok(crawlResult); // 성공적으로 크롤링한 결과 반환
        } catch (IllegalArgumentException e) {
            // 잘못된 URL일 경우 BAD_REQUEST 응답을 반환
            return ResponseEntity.badRequest().body("잘못된 URL 형식입니다.");
        } catch (Exception e) {
            // 크롤링 중 예외가 발생할 경우, INTERNAL_SERVER_ERROR 응답을 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("크롤링 중 오류가 발생했습니다.");
        }
    }

    // URL 유효성 검사 메서드
    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI(); // URL -> URI로 변환해서 유효성을 검사
            return true;          // 유효한 URL일 경우 TRUE을 반환
        } catch (Exception e) {
            return false;         // 예외가 발생한 경우 FALSE을 반환
        }
    }
}