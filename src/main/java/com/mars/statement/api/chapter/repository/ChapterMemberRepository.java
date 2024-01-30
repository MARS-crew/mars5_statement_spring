package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ChapterMemberRepository extends JpaRepository<ChapterMember, Long> {
    @Query
    ChapterMember findByChapterIdAndGroupMember_Id(Long chapterId, Long memberId);

}
