package com.example.echo.domain.member.dto.response;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long memberId;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String avatarImage;
    private Role role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .avatarImage(member.getAvatarImage())
                .role(member.getRole())
                .build();
    }
}
