package com.mars.statement.api.group.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateResponse {
    private Long groupId;
    private String groupName;
    private String groupImg;
}
