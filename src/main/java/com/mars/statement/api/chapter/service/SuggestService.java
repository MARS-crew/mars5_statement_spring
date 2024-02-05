package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;

import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestService {

    private final SuggestRepository suggestRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final ChapterService chapterService;
    public List<Suggest> getSuggestByGroupId(Long group_id){ return suggestRepository.findByGroupId(group_id); }
    public Suggest getSuggestById(Long suggest_id){ return suggestRepository.findById(suggest_id).orElse(null);}

    @Transactional
    public ResponseEntity<?> createSuggest(SuggestDto suggestDto) throws Exception {
        Group group = groupService.findGroupById(suggestDto.getGroupId());
        GroupMember groupMember = groupMemberService.findGroupMemberById(suggestDto.getConstructorId());

        Suggest savedSuggest = suggestRepository.save(Suggest.builder()
                .group(group)
                .suggest(suggestDto.getSuggest())
                .type(suggestDto.getType())
                .groupMember(groupMember)
                .build());

        CreateChapterDto createChapterDto = CreateChapterDto.builder()
                .constructorId(suggestDto.getConstructorId())
                .suggestId(savedSuggest.getId())
                .groupId(suggestDto.getGroupId())
                .memberIds(suggestDto.getMemberIds())
                .build();

        return chapterService.createChapterAndAddMembers(createChapterDto);
    }
}

