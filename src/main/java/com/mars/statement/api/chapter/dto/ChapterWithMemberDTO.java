package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mars.statement.api.chapter.domain.Chapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterWithMemberDTO {

    private Long chapterId;
    private String suggest;
    private String type;

    @JsonProperty("chapterMembers")
    private List<ChapterMemberDTO> chapterMembers;
}
