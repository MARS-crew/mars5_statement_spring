package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;

import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;
    private final ChapterRepository chapterRepository;
    private final GroupMemberRepository groupMemberRepository;


    public ChapterMember findChapterMemberById(Long chapter_id, Long member_id) {
        return chapterMemberRepository.findByChapterIdAndGroupMember_Id(chapter_id, member_id);
    }

    @Transactional
    public void addMemberToChapter(Long chapterId, Long constructorId, List<Long> memberIds) throws NotFoundException {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new NotFoundException("Chapter not found"));

        for (Long memberId : memberIds) {
            GroupMember member = groupMemberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException("Group member not found"));

            Boolean isConstructor = memberId.equals(constructorId); // 생성자 ID와 현재 회원 ID가 일치하는지 확인

            ChapterMember chapterMember = ChapterMember.builder()
                    .chapter(chapter)
                    .groupMember(member)
                    .constructor(isConstructor) // 생성자 여부 설정
                    .build();

            chapterMemberRepository.save(chapterMember);
        }
    }
}