package com.mars.statement.api.share.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberOpinionDTO {
    private Long memberId;
    private String memberName;
    private String memberImg;
//    private Set<OpinionDTO> opinionDTOSet = new HashSet<>();

    private OpinionDTO opinionDTO;
//    private List<OpinionDTO> opinionDTOList = new ArrayList<>();

//    public void addOpinion(OpinionDTO opinionDTO){
//        this.opinionDTOSet.add(opinionDTO);
//    }
    @Transient
    private List<OpinionDTO> opinionDTOList;

    public MemberOpinionDTO(Long memberId, String memberName, String memberImg, OpinionDTO opinionDTO) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionDTO = opinionDTO;
    }
    public MemberOpinionDTO(Long memberId, String memberName, String memberImg, List<OpinionDTO> opinionDTOList) {
        this.memberId = memberId; this.memberName=memberName;
        this.memberImg = memberImg; this.opinionDTOList = opinionDTOList;
    }
}
