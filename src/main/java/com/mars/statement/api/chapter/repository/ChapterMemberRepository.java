package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChapterMemberRepository extends JpaRepository<ChapterMember, Long> {
    @Query
    ChapterMember findByChapterIdAndGroupMember_Id(@Param("chapterId") Long chapterId, @Param("memberId") Long memberId);

    @Query("SELECT cm FROM ChapterMember cm WHERE cm.chapter.id = :chapterId AND cm.groupMember.user.id = :myId")
    ChapterMember findChapterMemberByChapterIdAndUserId(@Param("chapterId") Long chapterId, Long myId);

    @Query("SELECT COUNT(cm) > 0 FROM ChapterMember cm WHERE cm.chapter.id = :chapterId AND cm.id = :memberId")
    boolean existsByChapterIdAndMemberId(@Param("chapterId") Long chapterId, Long memberId);

    List<ChapterMember> findByChapter(Chapter chapter);

}
