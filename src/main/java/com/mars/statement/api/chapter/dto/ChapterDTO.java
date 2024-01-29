package com.mars.statement.api.chapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {

    private Long chapterId;
    private Long suggestId;
    private String suggest;
    private ShareMemberDTO memberDTOList;



}