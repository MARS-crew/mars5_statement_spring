package com.mars.statement.api.share.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareDetailDto {

    private Long suggestId;
    private String suggest;
    private Timestamp regDt;
    private Long chapterId;
    private String summary;

    @JsonIgnore
    private ShareMemberDetailDto shareMemberDetailDto;
    @Transient
    private List<ShareMemberDetailDto> memberDetailList;

    public ShareDetailDto(Long suggestId, String suggest, Long chapterId, Timestamp regDt, String summary, ShareMemberDetailDto shareMemberDetailDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterId = chapterId; this.regDt = regDt; this.summary = summary;
        this.shareMemberDetailDto = shareMemberDetailDto;
    }

    public ShareDetailDto(Long suggestId, String suggest, Long chapterId, Timestamp regDt, String summary, List<ShareMemberDetailDto> memberDetailList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterId = chapterId; this.regDt = regDt; this.summary = summary;
        this.memberDetailList = memberDetailList;
    }

}
