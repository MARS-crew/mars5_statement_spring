package com.mars.statement.api.share.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareDetailDto {

    private Long suggestId;
    private String suggest;
    private Long chapterId;
    private String summary;

    @JsonIgnore
    private ShareMemberDetailDto shareMemberDetailDto;
    @Transient
    private List<ShareMemberDetailDto> memberDetailList;

    public ShareDetailDto(Long suggestId, String suggest, Long chapterId, String summary, ShareMemberDetailDto shareMemberDetailDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterId = chapterId; this.summary = summary;
        this.shareMemberDetailDto = shareMemberDetailDto;
    }

    public ShareDetailDto(Long suggestId, String suggest, Long chapterId, String summary, List<ShareMemberDetailDto> memberDetailList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.chapterId = chapterId; this.summary = summary;
        this.memberDetailList = memberDetailList;
    }

}
