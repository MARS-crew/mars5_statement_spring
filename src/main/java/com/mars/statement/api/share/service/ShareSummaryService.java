package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.share.dto.ShareSummaryDto;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShareSummaryService {
    private final ChapterMemberRepository chapterMemberRepository;

    @Transactional
    public ResponseEntity<?> summaryShare(Long chapterId,ShareSummaryDto shareSummaryDto,Long myId) throws ForbiddenException {
        String summary = shareSummaryDto.getSummary();

        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId);

        // ChapterMember 엔티티가 없거나 생성자가 아니면 권한이 없음
        if (!(chapterMember.getIs_constructor() == 1)) {
            throw new ForbiddenException("You are not authorized to share summary for this chapter.");
        }

        chapterMember.setSummary(summary);
        chapterMemberRepository.save(chapterMember);

        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "서머리 작성 성공");
    }
}
