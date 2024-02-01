package com.mars.statement.api.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateRequest {
    private String name;
    private String img;

    private List<String> memberEmail;

}
