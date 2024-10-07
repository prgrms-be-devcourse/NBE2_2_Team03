package com.example.echo.domain.member.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String avatarImage;
    private Role role;

    public Member toMember() {
        Member member = new Member();
        member.setUserId(this.userId);
        member.setName(this.name);
        member.setEmail(this.email);
        member.setPassword(this.password);
        member.setPhone(this.phone);
        member.setAvatarImage(this.avatarImage != null ? this.avatarImage : "/images/default-avatar.png");
        member.setRole(this.role);
        return member;
    }
}
