package com.mars.statement.api.share.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareDetailDto {

    private Long suggestId;
    private String suggest;

    @Transient
    private Long chapterTimes; // 회차

    private Long chapterId;
    private String summary;

    @JsonIgnore
    private ShareMemberDtailDto shareMemberDtailDto;

    @Transient
    private List<ShareMemberDtailDto> shareMemberList;

    public ShareDetailDto(Long suggestId, String suggest, Long chapterTimes, Long chapterId, List<ShareMemberDtailDto> shareMemberList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterTimes = chapterTimes; this.chapterId = chapterId;
        this.shareMemberList = shareMemberList;
    }
}
