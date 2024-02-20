package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSuggestDto {

    private Long groupId;
    private Long suggestId;
    private Long constructorId;

    @JsonProperty("memberIds")
    private List<Long> memberIds;
}