package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestRepository extends JpaRepository<Suggest, Long> {
}
