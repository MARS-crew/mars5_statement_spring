package com.mars.statement.api.share.repository;

import com.mars.statement.api.share.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByShareId(Long shareId);

}
