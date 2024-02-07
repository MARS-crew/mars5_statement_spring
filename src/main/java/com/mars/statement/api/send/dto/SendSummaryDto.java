package com.mars.statement.api.send.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendSummaryDto {
    private Long chapterId;
    private Long memberId;
    private String summary;

}
