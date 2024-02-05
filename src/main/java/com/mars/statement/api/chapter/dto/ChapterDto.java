package com.mars.statement.api.chapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {
    private Long chapterId;
    private Long suggestId;
    private String suggest;
    private ShareMemberDto memberDtoList;



}