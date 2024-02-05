package com.mars.statement.api.group.service;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.domain.Invitation;
import com.mars.statement.api.group.dto.*;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import com.mars.statement.api.group.repository.GroupRepository;
import com.mars.statement.api.group.repository.InvitationRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.ForbiddenException;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final SuggestService suggestService;

    public Group findGroupById(Long id){
        return groupRepository.findById(id).orElse(null);
    }
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

        constructor.updateLastGroupId(group);

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

    public ResponseEntity<?> getMainPage(Long groupId, UserDto userDto) throws Exception {
        if(groupId == null || groupId == 0){
            User user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."));
            return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "속해있는 그룹이 없습니다.");
        }

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "존재하지 않는 그룹입니다."));

        GroupMember member = Optional.ofNullable(groupMemberRepository.findByGroupIdAndUserId(group.getId(), user.getId()))
                .orElseThrow(() -> new ForbiddenException("해당 그룹에 접근 권한이 없습니다."));

        List<GroupMember> myGroups = groupMemberRepository.findByUser(user);
        List<MyGroupsDto> myGroupsDto = new ArrayList<>();

        for(GroupMember myGroup : myGroups){
            myGroupsDto.add(MyGroupsDto.builder()
                    .groupId(myGroup.getGroup().getId())
                    .img(myGroup.getGroup().getImg())
                    .name(myGroup.getGroup().getName())
                    .build());
        }

        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);
        List<GroupMembersDto> groupMembersDto = new ArrayList<>();

        for (GroupMember groupMember : groupMembers) {
            groupMembersDto.add(GroupMembersDto.builder()
                    .groupMemberId(groupMember.getId())
                    .name(groupMember.getUser().getName())
                    .img(groupMember.getUser().getImg())
                    .isConstructor(groupMember.getConstructor())
                    .build());
        }

        List<Suggest> suggests = suggestService.getSuggestByGroupId(groupId);
        List<GroupSuggestsDto> groupSuggestsDto = new ArrayList<>();

        for (Suggest suggest : suggests) {
            groupSuggestsDto.add(GroupSuggestsDto.builder()
                    .suggestId(suggest.getId())
                    .type(suggest.getType())
                    .suggest(suggest.getSuggest())
                    .regDt(suggest.getRegDate())
                    .build());
        }

        user.updateLastGroupId(group);
        userRepository.save(user);

        return CommonResponse.createResponse(HttpStatus.OK.value(), group.getName() + " 조회 성공",
                MainPageResponse.builder()
                        .myGroups(myGroupsDto)
                        .groupMembers(groupMembersDto)
                        .groupSuggests(groupSuggestsDto)
                        .build());
    }

    private void createOrInviteMember(String memberEmail, Group group){
        Optional<User> userOptional = userRepository.findByEmail(memberEmail);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            groupMemberRepository.save(GroupMember.builder()
                    .group(group)
                    .user(user)
                    .constructor(false).build());
            user.updateLastGroupId(group);
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
