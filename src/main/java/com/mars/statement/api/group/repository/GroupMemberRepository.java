package com.mars.statement.api.group.repository;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {


    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroup(Group group);
    GroupMember findByGroupIdAndUserId(Long groupId, Long userId);
    GroupMember findByUserIdAndGroupId(Long user_Id,Long group_id);
}
