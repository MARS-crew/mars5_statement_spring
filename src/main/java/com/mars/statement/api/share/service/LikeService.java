package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.share.domain.Like;
import com.mars.statement.api.share.repository.LikeRepository;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ChapterMemberService chapterMemberService;
    private final ChapterService chapterService;
    private final ShareService shareService;

    public Like getLikeById(Long shareId){
        return likeRepository.findByShareId(shareId).orElse(null);
    }

    public int updateLike(Long shareId, Long chapterId, Long myId) throws NotFoundException {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(),myId);
        Like like = getLikeById(shareId);
        if (like != null) {
            likeRepository.delete(like);
            return 0;
        } else {
            Like newLike = Like.builder()
                    .share(shareService.getShareById(shareId))
                    .member(member)
                    .like(true) // 좋아요로 설정
                    .build();
            likeRepository.save(newLike);
            return 1;
        }


    }

}
