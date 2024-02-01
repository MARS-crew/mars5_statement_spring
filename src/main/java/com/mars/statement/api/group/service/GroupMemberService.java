package com.mars.statement.api.group.service;

import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;


    public GroupMemberService(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    public GroupMember getGroupMemberByGroupIdAndUser(Long group_id, Long user_id) {
        return groupMemberRepository.findByGroupIdAndUser_Id(group_id, user_id);
    }
}
