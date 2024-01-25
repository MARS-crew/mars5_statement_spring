package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import jakarta.persistence.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    @Query("SELECT c FROM Chapter c Left JOIN FETCH c.chapterMembers cm LEFT JOIN FETCH cm.groupMember gm LEFT JOIN FETCH gm.user WHERE c.id = :chapterId")
    Chapter findChapterWithMembers(@Param("chapterId") Long chapterId);
}