package com.mars.statement.api.share.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareOpinionDto {
    private Long chapterMemberId;
    private String opinion;
    private String location;
}
