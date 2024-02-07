package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import com.mars.statement.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;
    private final ChapterRepository chapterRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void addMemberToChapter(Long chapterId, Long myId, List<Long> memberIds) throws NotFoundException {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new NotFoundException(404, "Chapter not found"));

        for (Long memberId : memberIds) {
            GroupMember member = groupMemberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException(404, "Group member not found"));

            // 생성자 여부 설정: 로그인한 사용자의 ID와 멤버의 ID가 일치하면 생성자로 설정
            Long isConstructor = (memberId.equals(myId) ? 1L : 0L);

            ChapterMember chapterMember = ChapterMember.builder()
                    .chapter(chapter)
                    .groupMember(member)
                    .is_constructor(isConstructor)
                    .build();

            chapterMemberRepository.save(chapterMember);
        }
    }

    public ChapterMember getChapterMemberById(Long chapterId, Long memberId){
        return chapterMemberRepository.findByChapterIdAndGroupMember_Id(chapterId, memberId);
    }

    public ChapterMember getChapterMemberByChapterIdAndUserId(Long chapterId, Long myId){
        return chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId);
    }



}
