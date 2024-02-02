package com.mars.statement.api.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShareMemberDto {
    private Long memberId;
    private String memberName;
    private String email;
    private Timestamp regDt;
    private String opinion;
    private String location;

}
