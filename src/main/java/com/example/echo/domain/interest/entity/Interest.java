//package com.example.echo.domain.interest.entity;
//
//import com.example.echo.domain.member.entity.Member;
//import com.example.echo.domain.petition.entity.Petition;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "interest")
//@ToString
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Interest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "interest_id", nullable = false, unique = true)
//    private Long interestId;
//
//    @ManyToOne
//    @JoinColumn(name = "petition_id", nullable = false)
//    private Petition petition;  // 청원 필드 추가. N:1 연결
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//}