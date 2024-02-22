package com.mars.statement.api.share.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareMemberDetailDto {

    private Long opinionId;
    private Long memberId;
    private String memberName;
    private String opinion;
    private String location;
    private Boolean like;

}
