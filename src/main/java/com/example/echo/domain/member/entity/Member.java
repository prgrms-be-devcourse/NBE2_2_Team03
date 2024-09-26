package com.example.echo.domain.member.entity;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.interest.entity.Interest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "username", nullable = false, length = 255, unique = true)
    private String username;  // 로그인 유저아이디 추가

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "avatar_image", length = 255)
    private String avatarImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Interest> interestList = new ArrayList<>();    // 객체 생성 시 빈 리스트 초기화

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Inquiry> inquiryList = new ArrayList<>();      // Member 객체 생성 시 1:1 문의 비어 있는 리스트 초기화

    public void addInquiry(Inquiry inquiry) {
        inquiryList.add(inquiry);
    }
}
