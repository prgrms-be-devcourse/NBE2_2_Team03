package com.example.echo.domain.member.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRequest {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String avatarImage;
    private Role role;

    public void updateMember(Member member) {
        member.setUserId(this.userId);
        member.setName(this.name);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setAvatarImage(this.avatarImage != null ? this.avatarImage : "/images/avatar-default.png");
        member.setRole(this.role);
    }
}
