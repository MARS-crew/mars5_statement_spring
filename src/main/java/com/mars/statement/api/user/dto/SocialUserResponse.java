package com.mars.statement.api.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SocialUserResponse {
    private String id;
    private String email;
    private String name;
    private String picture;
}
