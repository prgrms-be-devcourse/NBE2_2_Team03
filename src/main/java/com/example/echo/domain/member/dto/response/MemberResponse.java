package com.example.echo.domain.member.dto.response;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "사용자 ID", example = "user1")
    private String userId;

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 이메일", example = "user1@example.com")
    private String email;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "회원 프로필 이미지 URL", example = "/images/default-avatar.png")
    private String avatarImage;

    @Schema(description = "회원 역할", example = "USER")
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
