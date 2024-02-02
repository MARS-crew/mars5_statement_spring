package com.mars.statement.api.send.dto;


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

public class PersonalSendDto {

    private Long suggestId;
    private String suggest;

    @JsonIgnore
    private MemberMessageDto memberMessageDto;

    @Transient
    private List<MemberMessageDto> memberMessageDtoList;

    public PersonalSendDto(Long suggestId, String suggest, MemberMessageDto memberOpinionDto){
        this.suggestId = suggestId; this.suggest = suggest;
        this.memberMessageDto = memberOpinionDto;
        this.memberMessageDtoList = Collections.singletonList(memberMessageDto);
    }

}

