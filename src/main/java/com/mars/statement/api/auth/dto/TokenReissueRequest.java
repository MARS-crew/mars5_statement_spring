package com.mars.statement.api.auth.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenReissueRequest {
    private String refreshToken;
}
