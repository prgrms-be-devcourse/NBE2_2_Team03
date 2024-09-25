package com.example.nbe2_2_team03.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "petition")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Petition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petition_id")
    private Long petitionId;

    @ManyToOne(fetch = FetchType.LAZY)  // Member 객체와 N:1 연결. 회원 삭제 시 청원도 삭제되는 cascade 논의 필요
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false, length = 1000)    // 청원서 작성 시 제목 길이 제한 1000 Byte
    private String title;

    @Column(name = "content", nullable = false, length = 4000)  // 청원서 작성 시 내용 길이 제한 4000 Byte
    private String content;

    @Column(name = "summary", length = 4000)    // 청원 객체 생성 시점엔 gpt api의 데이터를 받아오지 않아 nullable = true 설정
    private String summary;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "related_news", nullable = false)
    private String relatedNews;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Column(name = "interest_count", nullable = false)
    private Integer interestCount = 0;

    @Column(name = "agree_count")   // 청원 객체 생성 시점엔 동의자수 크롤링 데이터를 받아오지 않아 nullable = true 설정
    private Integer agreeCount;
}
