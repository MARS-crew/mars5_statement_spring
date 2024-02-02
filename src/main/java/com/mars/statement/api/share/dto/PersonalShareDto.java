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
public class PersonalShareDto {

    private Long suggestId;
    private String suggest;

    @JsonIgnore
    private MemberOpinionDto memberOpinionDto;

    @Transient
    private List<MemberOpinionDto> memberOpinionDtoList;


    public PersonalShareDto(Long suggestId, String suggest, MemberOpinionDto memberOpinionDto){
        this.suggestId = suggestId; this.suggest = suggest; this.memberOpinionDto = memberOpinionDto;
        this.memberOpinionDtoList = Collections.singletonList(memberOpinionDto);  // 수정된 부분

    }





}

