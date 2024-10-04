package com.example.echo.domain.member.entity;

import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.interest.entity.Interest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name = "user_id", nullable = false, length = 255, unique = true)
    private String userId;  // 로그인 유저아이디 추가 username을  userId로 변경

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

    // member 데이터를 삭제하려면 그 멤버와 관련된 inquiry 데이터를 먼저 처리해야 한다.
    // 데이터베이스에서 부모 테이블인 member 테이블의 데이터를 삭제하려 할 때,
    // 자식 테이블인 inquiry에서 여전히 참조 중인 경우 제약 조건에 의해 삭제 거부되므로 이 문제를 해결하기 위해 cascade 조건을 추가하였습니다.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Inquiry> inquiryList = new ArrayList<>();      // Member 객체 생성 시 1:1 문의 비어 있는 리스트 초기화

    public void addInquiry(Inquiry inquiry) {
        inquiryList.add(inquiry);
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", avatarImage='" + avatarImage + '\'' +
                ", role=" + role +
                ", createdDate=" + createdDate +
                ", interestListSize=" + interestList.size() +
                ", inquiryListSize=" + inquiryList.size() +
                '}';
    }

    // JWT 문자의 내용 반환
    public Map<String, Object> getPayload() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("memberId", memberId);
        payloadMap.put("userId", userId);
        payloadMap.put("name", name);
        payloadMap.put("email", email);
        payloadMap.put("role", role);
        return payloadMap;
    }
}
