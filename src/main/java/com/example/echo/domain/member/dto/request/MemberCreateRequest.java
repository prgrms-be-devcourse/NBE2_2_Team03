package com.example.echo.domain.member.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {

    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
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
