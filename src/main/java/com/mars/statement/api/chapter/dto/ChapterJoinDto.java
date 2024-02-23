package com.mars.statement.api.chapter.dto;

import com.mars.statement.api.chapter.domain.ChapterMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterJoinDto {
    private Long userId;
    private String name;
}
