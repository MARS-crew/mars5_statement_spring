package com.mars.statement.api.group.repository;

import com.mars.statement.api.group.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    GroupMember findByGroupIdAndUser_Id(Long group_id, Long user_Id);
}
