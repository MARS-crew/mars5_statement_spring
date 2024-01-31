package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateChapterDto {

    private Long groupId;
    private Long suggestId;
    private Long constructorId;

    @JsonProperty("memberIds")
    private List<Long> memberIds;
}