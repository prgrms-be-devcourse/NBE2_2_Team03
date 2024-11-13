package com.example.echo.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginRequest {

    @Schema(description = "사용자 ID", example = "user123")
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String userId;

    @Schema(description = "사용자 비밀번호", example = "1111")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
