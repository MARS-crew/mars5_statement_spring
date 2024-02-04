package com.mars.statement.api.share.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterSummaryDto {

    private Long chapterId;
    private Timestamp regDt;
    private String memberName;
    private String summary;
}
