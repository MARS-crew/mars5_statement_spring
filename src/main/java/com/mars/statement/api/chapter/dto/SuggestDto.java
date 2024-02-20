package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuggestDto {

    private Long groupId;
    private String suggest;
    private String type;

    @JsonProperty("memberIds")
    private List<Long> memberIds;
}