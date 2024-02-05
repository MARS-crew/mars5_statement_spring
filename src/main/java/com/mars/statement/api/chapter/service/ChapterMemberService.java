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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;
    private final ChapterRepository chapterRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void addMemberToChapter(Long chapterId, Long constructorId,List<Long> memberIds) throws NotFoundException {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new NotFoundException(404,"Chapter not found"));

        for (Long memberId : memberIds) {
            GroupMember member = groupMemberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException(404,"Group member not found"));

            Boolean isConstructor = memberId.equals(constructorId); // 생성자 ID와 현재 회원 ID가 일치하는지 확인

            ChapterMember chapterMember = ChapterMember.builder()
                    .chapter(chapter)
                    .groupMember(member)
                    //.is_constructor(isConstructor) // 생성자 여부 설정
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
