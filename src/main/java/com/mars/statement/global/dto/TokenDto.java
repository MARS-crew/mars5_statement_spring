package com.mars.statement.global.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
