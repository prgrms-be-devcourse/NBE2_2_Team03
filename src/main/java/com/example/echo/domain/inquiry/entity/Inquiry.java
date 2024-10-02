package com.example.echo.domain.inquiry.entity;

import com.example.echo.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id", nullable = false, unique = true)
    private Long inquiryId;

    @ManyToOne(fetch = FetchType.LAZY)  // Member 객체와 N:1 연결
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_category", nullable = false)
    private InquiryCategory inquiryCategory;

    @Column(name = "inquiry_title", nullable = false)
    private String inquiryTitle;

    @Column(name = "inquiry_content", length = 2000, nullable = false)
    private String inquiryContent;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "reply_content", length = 2000)  // 문의 등록 시 관리자 답변 null
    private String replyContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", nullable = false)
    @Builder.Default
    private InquiryStatus inquiryStatus = InquiryStatus.PENDING;    // 문의 등록 시 "답변 대기 중" 기본값

    @Column(name = "replied_date")
    private LocalDateTime repliedDate;  // 관리자가 답변 시 changeReplyContent()를 통해 갱신

    public void changeInquiryCategory(InquiryCategory inquiryCategory) {
        this.inquiryCategory = inquiryCategory;
    }

    public void changeInquiryTitle(String inquiryTitle) {
        this.inquiryTitle = inquiryTitle;
    }

    public void changeInquiryContent(String inquiryContent) {
        this.inquiryContent = inquiryContent;
    }

    // 관리자 답변 내용 추가/수정 메서드. 답변 시점에 답변 상태와 답변 작성일 갱신
    public void changeReplyContent(String replyContent) {
        this.replyContent = replyContent;
        this.inquiryStatus = InquiryStatus.RESOLVED;
        this.repliedDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Inquiry{" +
                "inquiryId=" + inquiryId +
                ", memberId=" + member.getMemberId() +
                ", inquiryCategory=" + inquiryCategory +
                ", inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryContent='" + inquiryContent + '\'' +
                ", createdDate=" + createdDate +
                ", replyContent='" + replyContent + '\'' +
                ", inquiryStatus=" + inquiryStatus +
                ", repliedDate=" + repliedDate +
                '}';
    }
}