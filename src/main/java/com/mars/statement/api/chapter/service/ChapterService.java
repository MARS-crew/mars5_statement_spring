package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDto;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDto;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;

    private final ModelMapper modelMapper;


    @Autowired
    public ChapterService(ChapterRepository chapterRepository, GroupMemberService groupMemberService, SuggestService suggestService, ModelMapper modelMapper) {
        this.chapterRepository = chapterRepository;
        this.groupMemberService = groupMemberService;
        this.suggestService = suggestService;
        this.modelMapper = modelMapper;
    }

    public Chapter findChapterById(Long id){
        return chapterRepository.findById(id).orElse(null);
    }

    public ChapterWithMemberDto getChapterWithMembers(Long chapter_id) {

        Chapter chapter = chapterRepository.findChapterWithMembers(chapter_id);

        if(chapter != null) {
            List<ChapterMemberDto> chapterMemberDtoList = chapter.getChapterMembers()
                    .stream()
                    .map(chapterMember -> new ChapterMemberDto(
                            chapterMember.getGroupMember().getId(),
                            chapterMember.getSummary(),
                            chapterMember.getGroupMember().getUser().getName()
                    )).toList();
            return new ChapterWithMemberDto(chapter.getId(), chapter.getSuggest().getSuggest(),chapter.getSuggest().getType(), chapterMemberDtoList);
        }

        return null;
    }

    public List<Chapter> getChaptersByMemberId(Long group_id, Long my_id, Long suggest_id){

        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(group_id,my_id);
        Suggest suggest = suggestService.getSuggestById(suggest_id);

        List<ChapterMember> chapterMembers = chapterRepository.findChaptersByMemberId(member.getId(), suggest_id);

        List<Chapter> chapters = new ArrayList<>();;
        for(ChapterMember chapterMember: chapterMembers){
            chapters.add(chapterMember.getChapter());
        }
        return chapters;
    }


}
