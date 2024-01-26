package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.ShareDto;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class ShareService {
    private final ShareRepository shareRepository;
    private final ChapterMemberRepository chapterMemberRepository;

    @Autowired
    public ShareService(ShareRepository shareRepository, ChapterMemberRepository chapterMemberRepository) {
        this.shareRepository = shareRepository;
        this.chapterMemberRepository = chapterMemberRepository;
    }

    @Transactional
    public RequestResponseDto<?> insertShare(ShareDto shareDto) {
        Long chapterMemberId = shareDto.getChapterMemberId();
        Optional<ChapterMember> optionalChapterMember = chapterMemberRepository.findById(chapterMemberId);

        if (optionalChapterMember.isEmpty()) {
            log.warn("존재하지 않는 ChapterMember입니다");
            throw new NotFoundException("존재하지 않는 ChapterMember입니다");
        }

        ChapterMember chapterMember = optionalChapterMember.get();

        Share share = Share.builder()
                .chapterMember(chapterMember)
                .opinion(shareDto.getOpinion())
                .reg_dt(new Timestamp(System.currentTimeMillis()))
                .location(shareDto.getLocation())
                .build();

        Share savedShare = shareRepository.save(share);

        ShareDto responseDto = ShareDto.builder()
                .id(savedShare.getId())
                .chapterMemberId(savedShare.getChapterMember().getId())
                .opinion(savedShare.getOpinion())
                .regDt(savedShare.getReg_dt())
                .location(savedShare.getLocation())
                .build();

        return RequestResponseDto.success(responseDto);
    }
}

