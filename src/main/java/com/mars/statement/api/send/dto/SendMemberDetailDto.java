package com.mars.statement.api.send.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendMemberDetailDto {
    private Long sendId;
    private Long fromId;
    private String memberName;
    private String message;
    private Timestamp regDt;
    private String location;
    private Boolean bookmark_yn;
}
