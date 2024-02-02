package com.mars.statement.api.send.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberMessageDto {
    private Long memberId;
    private String memberName;
    private String memberImg;
    private MessageDto messageDto;
    @Transient
    private List<MessageDto> messageDtoList;

    public MemberMessageDto(Long memberId, String memberName, String memberImg, MessageDto messageDto) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberImg = memberImg;
        this.messageDto = messageDto;
        this.messageDtoList = Collections.singletonList(messageDto);

    }

    public MemberMessageDto(Long memberId, String memberName, String memberImg, List<MessageDto> messageDtoList) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberImg = memberImg;
        this.messageDtoList = messageDtoList;
    }
}
