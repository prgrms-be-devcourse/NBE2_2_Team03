package com.example.nbe2_2_team03.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interest")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id", nullable = false, unique = true)
    private Long interestId;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}