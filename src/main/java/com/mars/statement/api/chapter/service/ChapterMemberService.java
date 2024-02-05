package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;


    public ChapterMember getChapterMemberById(Long chapterId, Long memberId){
        return chapterMemberRepository.findByChapterIdAndGroupMember_Id(chapterId, memberId);
    }

    public ChapterMember getChapterMemberByChapterIdAndUserId(Long chapterId, Long myId){
        return chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId);
    }



}
