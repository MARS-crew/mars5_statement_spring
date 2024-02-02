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
    private List<OpinionDto> opinionDtoList;

    public MemberOpinionDto(Long memberId, String memberName, String memberImg, OpinionDto opinionDto) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionDto = opinionDto;
        this.opinionDtoList = Collections.singletonList(opinionDto); // 수정된 부분

    }
    public MemberOpinionDto(Long memberId, String memberName, String memberImg, List<OpinionDto> opinionDtoList) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionDtoList = opinionDtoList;
    }
}
