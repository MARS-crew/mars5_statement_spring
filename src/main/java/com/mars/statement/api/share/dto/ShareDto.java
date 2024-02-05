package com.mars.statement.api.share.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDto {
    private Long chapterMemberId;
    private String opinion;
    private String location;


    private Long suggestId;
    private String suggest;
    private MemberOpinionDTO memberOpinionDTO;


}


