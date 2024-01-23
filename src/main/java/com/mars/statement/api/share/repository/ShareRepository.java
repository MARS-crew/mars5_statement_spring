package com.mars.statement.api.share.repository;

import com.mars.statement.api.share.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {
}
