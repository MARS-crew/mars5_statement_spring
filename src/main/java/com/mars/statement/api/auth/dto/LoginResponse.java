package com.mars.statement.api.auth.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
}