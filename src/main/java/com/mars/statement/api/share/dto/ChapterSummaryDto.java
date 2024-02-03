package com.mars.statement.api.share.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterSummaryDto {

    Long chapterId;
    Timestamp regDt;
    String memberName;
    String summary;
}
