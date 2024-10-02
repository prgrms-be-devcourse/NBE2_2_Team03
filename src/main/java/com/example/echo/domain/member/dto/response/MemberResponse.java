package com.example.echo.domain.member.dto.response;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

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
    private String accessToken;
    private String refreshToken;

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

    public static MemberResponse from(Map<String, String> token) {
        return MemberResponse.builder()
                .accessToken(token.get("accessToken"))
                .refreshToken(token.get("refreshToken"))
                .build();
    }
}
