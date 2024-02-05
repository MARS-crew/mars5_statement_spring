package com.mars.statement.api.group.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyGroupsDto {
    private Long groupId;
    private String name;
    private String img;
}
