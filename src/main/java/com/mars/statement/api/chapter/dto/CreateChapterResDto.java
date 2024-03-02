package com.mars.statement.api.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateChapterResDto {
    private List<ChapterJoinDto> chapterJoinDto;
    private Long chapterId;
}
