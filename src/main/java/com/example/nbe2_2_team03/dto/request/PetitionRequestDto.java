package com.example.nbe2_2_team03.dto.request;

import com.example.nbe2_2_team03.entity.Category;
import com.example.nbe2_2_team03.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PetitionRequestDto {
    private Member member;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Category category;
    private String originalUrl;
    private String relatedNews;
    private Integer likesCount;
    private Integer interestCount;
    private Integer agreeCount;
}
