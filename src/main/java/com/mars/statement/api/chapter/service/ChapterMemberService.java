package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;

import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;
    private final ChapterRepository chapterRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Autowired
    public ChapterMemberService(ChapterMemberRepository chapterMemberRepository, ChapterRepository chapterRepository, GroupMemberRepository groupMemberRepository) {
        this.chapterMemberRepository = chapterMemberRepository;
        this.chapterRepository = chapterRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public ChapterMember findChapterMemberById(Long chapter_id, Long member_id){
        return chapterMemberRepository.findByChapterIdAndGroupMember_Id(chapter_id, member_id);
    }

    @Transactional
    public void addMemberToChapter(Long chapterId, Long memberId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        GroupMember member = groupMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Group member not found"));

        ChapterMember chapterMember = ChapterMember.builder()
                .chapter(chapter)
                .groupMember(member)
                .build();

        chapterMemberRepository.save(chapterMember);
    }


}
