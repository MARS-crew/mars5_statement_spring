package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query("SELECT c FROM Chapter c " +
            "LEFT JOIN FETCH c.suggest suggest" +
            "LEFT JOIN FETCH c.chapterMembers cm " +
            "LEFT JOIN FETCH cm.groupMember gm " +
            "LEFT JOIN FETCH gm.user " +
            "WHERE c.id = :chapterId")
    Chapter findChapterWithMembers(@Param("chapterId") Long chapterId);


    @Query("SELECT cm FROM ChapterMember cm " +
            "LEFT JOIN FETCH cm.chapter c " +
            "LEFT JOIN FETCH c.suggest s " +
            "WHERE cm.groupMember.id = :member_id AND s.id = :suggest_id")
    List<ChapterMember> findChaptersByMemberId(Long member_id, Long suggest_id);
}