package com.mars.statement.api.group.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersDto {
    private Long groupMemberId;
    private String name;
    private String img;
    private Boolean isConstructor;
}
