package com.mars.statement.api.chapter.repository;

import com.mars.statement.api.chapter.domain.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SuggestRepository extends JpaRepository<Suggest, Long> {
    List<Suggest> findByGroupId(@Param("group_id") Long group_id);
}
