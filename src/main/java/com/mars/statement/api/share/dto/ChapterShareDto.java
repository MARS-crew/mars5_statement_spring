package com.mars.statement.api.share.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterShareDto {

    Long suggestId;
    String suggest;

    @JsonIgnore
    ChapterSummaryDto chapterSummaryDto;

    @Transient
    List<ChapterSummaryDto> chapterShareDtoList;

    public ChapterShareDto(Long suggestId, String suggest, List<ChapterSummaryDto> chapterShareDtoList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterShareDtoList = chapterShareDtoList;
    }

}
