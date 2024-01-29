package com.mars.statement.api.share.dto;


import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {

    private Long suggestId;
    private String suggest;
    //    private Set<MemberOpinionDTO> memberOpinionDTOSet = new HashSet<>();
//    private List<MemberOpinionDTO> memberOpinionDTOList;
    private MemberOpinionDTO memberOpinionDTO;


//    public void addMemberOpinion(MemberOpinionDTO memberOpinionDTO){
//        this.memberOpinionDTOSet.add(memberOpinionDTO);
//    }
}

