package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;

    @Autowired
    public ChapterMemberService(ChapterMemberRepository chapterMemberRepository) {
        this.chapterMemberRepository = chapterMemberRepository;
    }

    public ChapterMember findChapterMemberById(Long chapter_id, Long member_id){
        return chapterMemberRepository.findByChapterIdAndGroupMember_Id(chapter_id, member_id);
    }

}
