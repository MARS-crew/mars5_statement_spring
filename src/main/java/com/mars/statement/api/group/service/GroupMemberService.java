package com.mars.statement.api.group.service;

import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    public GroupMember findGroupMemberById(Long groupMemberId){
        return groupMemberRepository.findById(groupMemberId).orElse(null);
    }
}
