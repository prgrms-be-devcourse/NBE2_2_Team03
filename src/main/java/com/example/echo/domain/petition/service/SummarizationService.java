package com.example.echo.domain.petition.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${openai.api.key}")
    private String apiKey;

    public String getSummarizedText(String text) {
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + apiKey );

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", "다음 국민청원을 요약하고 주요 포인트를 포함해 주세요: " + text));

        // body.put("prompt", "다음 텍스트를 요약해 주세요: " + text);
        body.put("messages", messages);
        body.put("max_tokens", 300);
        body.put("temperature", 0.3); // 높을 수록 유연

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers); // body, headers 하나로 만들기
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // 요약 내용 추출
        Map<String, Object> responseBody = response.getBody();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");

        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String summary = (String) message.get("content");

        return summary.trim();
    }

}
