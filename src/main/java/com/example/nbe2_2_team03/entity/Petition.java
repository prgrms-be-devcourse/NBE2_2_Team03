package com.example.nbe2_2_team03.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "petition")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Petition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petition_id")
    private Long petitionId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

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
