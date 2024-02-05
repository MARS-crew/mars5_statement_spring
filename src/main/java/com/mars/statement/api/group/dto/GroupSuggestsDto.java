package com.mars.statement.api.group.dto;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupSuggestsDto {
    private Long suggestId;
    private Timestamp regDt;
    private String type;
    private String suggest;
}
