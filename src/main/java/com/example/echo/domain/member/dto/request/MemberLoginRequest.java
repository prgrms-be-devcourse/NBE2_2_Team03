package com.example.echo.domain.member.dto.request;

import lombok.*;

@Getter
@Builder
public class MemberLoginRequest {

    private String userId;
    private String password;
}
