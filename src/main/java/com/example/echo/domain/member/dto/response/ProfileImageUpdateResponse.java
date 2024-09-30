package com.example.echo.domain.member.dto.response;

import com.example.echo.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class ProfileImageUpdateResponse {
    private final Long id;
    private final String userId;
    private final String name;
    private final String email;
    private final String phone;
    private final String avatarImage;

    // 생성자를 통해 모든 필드를 초기화
    public ProfileImageUpdateResponse(Long id, String userId, String name, String email, String phone, String avatarImage) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.avatarImage = avatarImage;
    }

    // Member 엔티티로부터 ProfileImageUpdateResponse 생성
    public static ProfileImageUpdateResponse from(Member member) {
        return new ProfileImageUpdateResponse(
                member.getMemberId(),
                member.getUserId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAvatarImage()
        );
    }
}