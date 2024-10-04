package com.example.echo.domain.petition.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SummarizationService {

    private final RestTemplate restTemplate;

    public SummarizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getSummarizedText(String text) {
        String url = "https://api.openai.com/v1/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("prompt", "다음 텍스트를 요약해 주세요: " + text);
        body.put("max_tokens", 100);
        body.put("temperature", 0.5);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers); // body, headers 하나로 만들기
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // 요약 내용 추출
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, String>> choices = (List<Map<String, String>>) responseBody.get("choices");
        String summary = choices.get(0).get("text");

        return summary.trim();
    }

}
