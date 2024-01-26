package com.mars.statement.api.share.dto;

import com.mars.statement.api.chapter.domain.ChapterMember;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDto {
    private Long id;
    private Long chapterMemberId;
    private String opinion;
    private Timestamp regDt;
    private String location;
}
