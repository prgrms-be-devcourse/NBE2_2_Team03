package com.example.echo.domain.member.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRequest {

    private String userId;

    private String name;

    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;

    private String avatarImage;

    private Role role;

    public void updateMember(Member member) {
        if (userId != null) member.setUserId(this.userId);
        if (name != null) member.setName(this.name);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setAvatarImage(this.avatarImage != null ? this.avatarImage : "/images/avatar-default.png");
        if (role != null) member.setRole(this.role);
    }
}
