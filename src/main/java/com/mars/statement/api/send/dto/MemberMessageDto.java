package com.mars.statement.api.send.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberMessageDTO {
    private Long memberId;
    private String memberName;
    private String memberImg;

    private MessageDTO messageDTO;

    @Transient
    private List<MessageDTO> messageDTOList;

    public MemberMessageDTO(Long memberId, String memberName, String memberImg, MessageDTO messageDTO) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.messageDTO = messageDTO;
    }

    public MemberMessageDTO(Long memberId, String memberName, String memberImg, List<MessageDTO> messageDTOList) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.messageDTOList = messageDTOList;
    }
}
