package com.example.echo.domain.petition.dto.request;

import com.example.echo.domain.petition.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetitionRequestDto {
    private Long memberId;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private String originalUrl;
    private String relatedNews;
}
