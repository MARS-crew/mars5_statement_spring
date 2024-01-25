package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.ChapterMemberDTO;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterMemberService {

    private final ChapterMemberRepository chapterMemberRepository;

    @Autowired
    public ChapterMemberService(ChapterMemberRepository chapterMemberRepository) {
        this.chapterMemberRepository = chapterMemberRepository;
    }

    public ChapterMember findChapterMemberById(Long id){
        return chapterMemberRepository.findById(id).orElse(null);
    }

}
