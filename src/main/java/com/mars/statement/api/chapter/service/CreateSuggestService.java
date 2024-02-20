package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.CreateSuggestDto;
import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.group.service.GroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateSuggestService {
    private final SuggestRepository suggestRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final CreateChapterService createChapterService;
    @Transactional
    public ResponseEntity<?> createSuggest(SuggestDto suggestDto,Long myId) throws Exception {
        Group group = groupService.findGroupById(suggestDto.getGroupId());
        GroupMember groupMember = groupMemberService.findGroupMemberByIdAndGroupId(myId, suggestDto.getGroupId());

        Suggest savedSuggest = suggestRepository.save(Suggest.builder()
                .group(group)
                .suggest(suggestDto.getSuggest())
                .type(suggestDto.getType())
                .groupMember(groupMember)
                .build());

        CreateSuggestDto createSuggestDto = CreateSuggestDto.builder()
                .constructorId(savedSuggest.getGroupMember().getId())
                .suggestId(savedSuggest.getId())
                .groupId(suggestDto.getGroupId())
                .memberIds(suggestDto.getMemberIds())
                .build();

        return createChapterService.createChapterAndAddMembers(createSuggestDto.getSuggestId(),createSuggestDto,myId);
    }
}