package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query("SELECT c FROM Chapter c " +
            "LEFT JOIN FETCH c.suggest suggest " +
            "LEFT JOIN FETCH c.chapterMembers cm " +
            "LEFT JOIN FETCH cm.groupMember gm " +
            "LEFT JOIN FETCH gm.user " +
            "WHERE c.id = :chapterId")
    Chapter findChapterWithMembers(@Param("chapterId") Long chapterId);


    @Query("SELECT cm FROM ChapterMember cm " +
            "LEFT JOIN FETCH cm.chapter c " +
            "LEFT JOIN FETCH c.suggest s " +
            "WHERE cm.groupMember.id = :memberId AND s.id = :suggestId")
    List<ChapterMember> findChaptersByMemberId(@Param("memberId") Long memberId, @Param("suggestId") Long suggestId);

    @Query("SELECT c.suggest.type FROM Chapter c WHERE c.id = :chapterId")
    String findChapterTypeById(@Param("chapterId") Long chapterId);
}

