package com.example.echo.domain.petition.dto.request;

import com.example.echo.domain.petition.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PetitionRequestDto {
    private Long memberId;  // Member 엔티티 대신 ID만 받음
    private String title;
    private String content;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private String originalUrl;
    private String relatedNews;
    // likesCount, interestCount, agreeCount는 제거 (서버에서 관리)
}