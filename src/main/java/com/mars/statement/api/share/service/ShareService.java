package com.mars.statement.api.share.service;


import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.ShareDto;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {
    private final ShareRepository shareRepository;
    private final ChapterMemberRepository chapterMemberRepository;

    @Transactional
    public ResponseEntity<?> insertShare(ShareDto shareDto) throws NotFoundException {
        Long chapterMemberId = shareDto.getChapterMemberId();
        Optional<ChapterMember> optionalChapterMember = chapterMemberRepository.findById(chapterMemberId);

        if (optionalChapterMember.isEmpty()) {
            log.warn("존재하지 않는 ChapterMember입니다");
            throw new NotFoundException("존재하지 않는 ChapterMember입니다.");
        }

        ChapterMember chapterMember = optionalChapterMember.get();

        // Share 엔티티를 데이터베이스에 저장
        shareRepository.save(Share.builder()
                .chapterMember(chapterMember)
                .opinion(shareDto.getOpinion())
                .location(shareDto.getLocation())
                .build());

        // createResponseMessage 메소드를 사용하여 응답 생성
        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "Share 생성 성공");
    }
}