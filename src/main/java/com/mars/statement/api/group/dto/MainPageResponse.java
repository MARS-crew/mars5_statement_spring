package com.mars.statement.api.group.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainPageResponse {
    private List<MyGroupsDto> myGroups;
    private List<GroupMembersDto> groupMembers;
    private List<GroupSuggestsDto> groupSuggests;
}
