package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckChapterDto {

    private Long suggestId;
    private String suggest;

    @JsonIgnore
    private ChapterSummaryDto chapterSummaryDto;

    @Transient
    private List<ChapterSummaryDto> summaryList;

    public CheckChapterDto(Long suggestId, String suggest, ChapterSummaryDto chapterSummaryDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterSummaryDto = chapterSummaryDto;
    }

    public CheckChapterDto(Long suggestId, String suggest, List<ChapterSummaryDto> summaryList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.summaryList = summaryList;
    }

}
