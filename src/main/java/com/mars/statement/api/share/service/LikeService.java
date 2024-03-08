package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.fcm.service.FcmService;
import com.mars.statement.api.share.domain.Like;
import com.mars.statement.api.share.repository.LikeRepository;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ChapterMemberService chapterMemberService;
    private final ChapterService chapterService;
    private final ShareService shareService;
    private final FcmService fcmService;

    public Like getLikeById(Long shareId){
        return likeRepository.findByShareId(shareId).orElse(null);
    }

    public int updateLike(Long chapterId, Long shareId, Long myId) throws NotFoundException, IOException {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(),myId);
        Like like = getLikeById(shareId);
        if (like != null) {
            System.out.println("like 있으면 shareId: " + like.getShare().getId());
            likeRepository.delete(like);
            return 0;
        } else {
            System.out.println("like 없으면 shareId: " + shareId);
            Like newLike = Like.builder()
                    .share(shareService.getShareById(shareId))
                    .member(member)
                    .like(true) // 좋아요로 설정
                    .build();
            likeRepository.save(newLike);
            fcmService.sendMessageTo(shareService.getShareById(shareId).getChapterMember().getGroupMember().getUser().getFcmToken(), "CO:MIT",
                    member.getUserName()+"님이 좋아요를 눌렀습니다.");

            return 1;
        }


    }

}
