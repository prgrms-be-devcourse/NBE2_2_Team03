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
public class MemberUpdateRequest {

    @Schema(description = "사용자 ID", example = "user123")
    private String userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일 주소", example = "example@example.com", required = true)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @Schema(description = "전화번호", example = "010-1234-5678", required = true)
    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;

    @Schema(description = "아바타 이미지 URL", example = "/images/user-avatar.png")
    private String avatarImage;

    @Schema(description = "사용자 권한", example = "USER")
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
