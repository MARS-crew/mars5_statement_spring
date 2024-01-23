package com.mars.statement.api.send.repository;

import com.mars.statement.api.send.domain.Send;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRepository extends JpaRepository<Send, Long> {
}
