package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final SuggestRepository suggestRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    private final ChapterService chapterService;



    @Transactional
    public Long createSuggest(SuggestDto suggestDto) {
        Group group = groupService.findGroupById(suggestDto.getGroupId());
        GroupMember groupMember = groupMemberService.findGroupMemberById(suggestDto.getConstructorId());

        Suggest suggest = Suggest.builder()
                .group(group)
                .suggest(suggestDto.getSuggest())
                .type(suggestDto.getType())
                .groupMember(groupMember)
                .build();

        Suggest savedSuggest = suggestRepository.save(suggest);
        //return savedSuggest.getId();
        Long chapterId = chapterService.createChapterAndAddMembers(savedSuggest.getId(), suggestDto.getConstructorId());


        return chapterId;
    }
}