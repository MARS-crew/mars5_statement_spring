package com.mars.statement.api.group.service;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.domain.Invitation;
import com.mars.statement.api.group.dto.GroupCreateRequest;
import com.mars.statement.api.group.dto.GroupCreateResponse;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import com.mars.statement.api.group.repository.GroupRepository;
import com.mars.statement.api.group.repository.InvitationRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    public ResponseEntity<?> createGroup(GroupCreateRequest request, UserDto userDto) throws Exception {
        User constructor = userRepository.findById(userDto.getId()).orElseThrow(()
                -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "존재하지 않은 사용자 입니다."));

        Group group = groupRepository.save(Group.builder()
                .name(request.getName())
                .img(request.getImg())
                .build());

        groupMemberRepository.save(GroupMember.builder()
                .group(group)
                .user(constructor)
                .constructor(true).build());


        List<String> memberEmails = request.getMemberEmail();
        for(String memberEmail:memberEmails){
            createOrInviteMember(memberEmail, group);
        }



        return CommonResponse.createResponse(HttpStatus.OK.value(), "그룹 생성 완료",
                GroupCreateResponse.builder()
                        .groupId(group.getId())
                        .groupName(group.getName())
                        .groupImg(group.getImg())
                        .build()
        );
    }

    private void createOrInviteMember(String memberEmail, Group group){
        Optional<User> userOptional = userRepository.findByEmail(memberEmail);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            groupMemberRepository.save(GroupMember.builder()
                    .group(group)
                    .user(user)
                    .constructor(false).build());
        }
        else {
            invitationRepository.save(Invitation.builder()
                    .group(group)
                    .email(memberEmail)
                    .accepted(false)
                    .build());
        }
    }

}
