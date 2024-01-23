package com.mars.statement.api.group.repository;

import com.mars.statement.api.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
