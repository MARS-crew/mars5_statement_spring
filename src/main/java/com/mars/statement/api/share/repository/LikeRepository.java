package com.mars.statement.api.share.repository;

import com.mars.statement.api.share.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
