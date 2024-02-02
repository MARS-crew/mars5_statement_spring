package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterWithMemberDto {

    private Long chapterId;
    private String suggest;
    private String type;

    @JsonProperty("chapterMembers")
    private List<ChapterMemberDto> chapterMembers;
}
