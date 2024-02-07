package com.mars.statement.api.share.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareSummaryDto {

    private Long chapterId;
    private Long memberId;
    private String summary;

}