package com.mars.statement.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER("ROLE_USER", "사용자"), ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String Key;
    private final String title;
}
