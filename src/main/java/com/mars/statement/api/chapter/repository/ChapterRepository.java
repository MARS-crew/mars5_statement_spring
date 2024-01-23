package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
