package com.example.echo.domain.member.dto;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long memberId;
    private String username;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatarImage;
    private Role role;

    public static MemberDto of(Member member){
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .phone(member.getPhone())
                .avatarImage(member.getAvatarImage())
                .role(member.getRole())
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .memberId(this.memberId)
                .username(this.username)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .phone(this.phone)
                .avatarImage(this.avatarImage)
                .role(this.role)
                .build();
    }
}
