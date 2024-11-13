package com.example.echo.domain.member.dto.request;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {

    @Schema(description = "사용자 ID", example = "user123")
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @Schema(description = "사용자 이메일", example = "example@example.com")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @Schema(description = "사용자 비밀번호", example = "1111")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Schema(description = "전화번호", example = "010-1234-5678")
    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;

    @Schema(description = "사용자 아바타 이미지 URL", example = "/images/user-avatar.png")
    private String avatarImage;

    @Schema(description = "사용자 역할", example = "USER")
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
