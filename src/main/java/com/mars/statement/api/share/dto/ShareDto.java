package com.mars.statement.api.share.dto;


import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShareDto {

    private Long suggestId;
    private String suggest;
    private MemberOpinionDto memberOpinionDto;

    @Transient
    private List<MemberOpinionDto> memberOpinionDtoList;


    public ShareDto(Long suggestId, String suggest, MemberOpinionDto memberOpinionDto){
        this.suggestId = suggestId; this.suggest = suggest; this.memberOpinionDto = memberOpinionDto;
    }

    public ShareDto(Long suggestId, String suggest, List<MemberOpinionDto> memberOpinionDtoList){
        this.suggestId=suggestId; this.suggest=suggest;
        this.memberOpinionDtoList = memberOpinionDtoList;
    }



}

