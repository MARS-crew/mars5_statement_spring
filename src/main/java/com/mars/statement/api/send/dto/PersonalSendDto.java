package com.mars.statement.api.send.dto;


import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalSendDTO {

    private Long suggestId;
    private String suggest;
    private MemberMessageDTO memberMessageDTO;

    @Transient
    private List<MemberMessageDTO> memberMessageDTOList;

    public PersonalSendDTO(Long suggestId, String suggest, MemberMessageDTO memberMessageDTO){
        this.suggestId = suggestId; this.suggest = suggest; this.memberMessageDTO = memberMessageDTO;
    }

    public PersonalSendDTO(Long suggestId, String suggest, List<MemberMessageDTO> memberMessageDTOList){
        this.suggestId = suggestId; this.suggest = suggest; this.memberMessageDTOList = memberMessageDTOList;
    }


}

