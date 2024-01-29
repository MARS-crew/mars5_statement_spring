package com.mars.statement.api.chapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterMemberDTO {

    private Long memberId;
    private String summary;

    @JsonProperty("userName")
    private String userName;
}
