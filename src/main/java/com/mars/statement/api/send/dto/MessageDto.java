package com.mars.statement.api.send.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long seq;
    private Long chapterId;
    private Long chapterMemberId;
    private Timestamp regDt;
    private String message;
    private String location;
}
