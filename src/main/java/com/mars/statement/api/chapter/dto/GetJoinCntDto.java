package com.mars.statement.api.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetJoinCntDto {
    private Integer joinCnt;
    private Integer memberCnt;
}
