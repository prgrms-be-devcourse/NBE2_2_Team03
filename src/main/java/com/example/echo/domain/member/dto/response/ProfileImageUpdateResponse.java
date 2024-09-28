package com.example.echo.domain.member.dto.response;

import com.example.echo.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImageUpdateResponse {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String avatarImage;

    // Member 엔티티로부터 ProfileImageUpdateResponse 생성
    public static ProfileImageUpdateResponse from(Member member) {
        ProfileImageUpdateResponse response = new ProfileImageUpdateResponse();
        response.setId(member.getMemberId());
        response.setUserId(member.getUserId());
        response.setName(member.getName());
        response.setEmail(member.getEmail());
        response.setPhone(member.getPhone());
        response.setAvatarImage(member.getAvatarImage());
        return response;
    }
}