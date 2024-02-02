package com.mars.statement.api.send.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class PersonalSendDto {

    private Long suggestId;
    private String suggest;

    private List<MemberMessageDto> memberMessageDtoList;

}

