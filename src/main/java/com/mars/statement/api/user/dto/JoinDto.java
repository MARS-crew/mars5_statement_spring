package com.mars.statement.api.user.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    private String email;
    private String name;
    private String picture;
}
