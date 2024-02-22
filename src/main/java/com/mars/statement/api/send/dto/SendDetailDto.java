package com.mars.statement.api.send.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mars.statement.api.share.dto.ShareMemberDetailDto;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendDetailDto {

    private Long suggestId;
    private String suggest;
    private Long seq;
    private Long chapterId;
    private String summary;

    @JsonIgnore
    private SendMemberDetailDto sendMemberDetailDto;
    @Transient
    private List<SendMemberDetailDto> memberDetailList;

    public SendDetailDto(Long suggestId, String suggest, Long seq, Long chapterId, String summary, SendMemberDetailDto sendMemberDetailDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.seq = seq; this.chapterId = chapterId; this.summary = summary;
        this.sendMemberDetailDto = sendMemberDetailDto;
    }
    public SendDetailDto(Long suggestId, String suggest, Long seq, Long chapterId, String summary, List<SendMemberDetailDto> memberDetailList){
        this.suggestId = suggestId; this.suggest = suggest;
        this.seq = seq; this.chapterId = chapterId; this.summary = summary;
        this.memberDetailList = memberDetailList;
    }



}
