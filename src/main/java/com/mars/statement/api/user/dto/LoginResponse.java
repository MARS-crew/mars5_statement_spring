package com.mars.statement.api.user.dto;

import com.mars.statement.global.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private UserRole role;
}
