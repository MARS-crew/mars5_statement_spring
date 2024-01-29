package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChapterMemberRepository extends JpaRepository<ChapterMember, Long> {
    @Query
    ChapterMember findByChapterIdAndGroupMember(Long chapterId, Long memberId);



}
