package com.mars.statement.api.share.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberOpinionDto {
    private Long memberId;
    private String memberName;
    private String memberImg;

    private OpinionDto opinionDto;

    @Transient
    private List<OpinionDto> opinionList;

    public MemberOpinionDto(Long memberId, String memberName, String memberImg, OpinionDto opinionDto) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionDto = opinionDto;
        this.opinionList = Collections.singletonList(opinionDto);

    }
    public MemberOpinionDto(Long memberId, String memberName, String memberImg, List<OpinionDto> opinionList) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionList = opinionList;
    }

    public MemberOpinionDto(List<OpinionDto> opinionList){
        this.opinionList = opinionList;
    }
}
