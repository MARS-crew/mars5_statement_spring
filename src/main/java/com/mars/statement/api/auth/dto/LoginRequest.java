package com.mars.statement.api.auth.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String uid;
    private String email;
    private String name;
    private String picture;
    private String fcmToken;
}
