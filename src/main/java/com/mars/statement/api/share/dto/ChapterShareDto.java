package com.mars.statement.api.share.dto;

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
public class ChapterShareDto {

    private Long suggestId;
    private String suggest;

    @JsonIgnore
    private ChapterSummaryDto chapterSummaryDto;

    @Transient
    private List<ChapterSummaryDto> chapterSummaryDtoList;

    public ChapterShareDto(Long suggestId, String suggest, ChapterSummaryDto chapterSummaryDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterSummaryDto = chapterSummaryDto;
        this.chapterSummaryDtoList = Collections.singletonList(chapterSummaryDto);
    }

    public ChapterShareDto(Long suggestId, String suggest, List<ChapterSummaryDto> chapterSummaryDtoList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterSummaryDtoList = chapterSummaryDtoList;
    }

}
