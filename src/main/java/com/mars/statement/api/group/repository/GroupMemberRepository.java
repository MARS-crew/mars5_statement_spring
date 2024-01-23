package com.mars.statement.api.group.repository;

import com.mars.statement.api.group.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
