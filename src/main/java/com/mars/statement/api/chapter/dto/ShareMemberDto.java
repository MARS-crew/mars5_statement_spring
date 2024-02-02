package com.mars.statement.api.chapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class ShareMemberDto {

    private Long memberId;
    private String memberName;
    private String email;
    private Timestamp regDt;
    private String opinion;
    private String location;
    public ShareMemberDto(Long memberId, String memberName, String email, Timestamp regDt, String opinion, String location) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.regDt = regDt;
        this.opinion = opinion;
        this.location = location;
    }
}