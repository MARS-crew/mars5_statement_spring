package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.ChapterMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterMemberRepository extends JpaRepository<ChapterMember, Long> {
}
