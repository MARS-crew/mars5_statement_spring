package com.mars.statement.api.group.service;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupMember getGroupMemberByGroupIdAndUser(Long groupId, Long userId) {
        return groupMemberRepository.findByGroupIdAndUserId(groupId, userId);
    }

    public List<Group> getMyGroups(Long userId) {
        List<Group> myGroups = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        List<GroupMember> myMemberLists = groupMemberRepository.findByUser(user);
        for (GroupMember myMemberList : myMemberLists){
            Group myGroup = myMemberList.getGroup();
            myGroups.add(myGroup);
        }
        return myGroups;
    }

    public GroupMember findGroupMemberById(Long groupMemberId){
        return groupMemberRepository.findById(groupMemberId).orElse(null);
    }

    public GroupMember findGroupMemberByIdAndGroupId(Long memberId, Long groupId) {
        return groupMemberRepository.findByUserIdAndGroupId(memberId, groupId);
    }
}