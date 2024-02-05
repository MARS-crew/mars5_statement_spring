package com.mars.statement.api.chapter.service;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDto;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDto;

import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;

    public Chapter getChapterById(Long id) throws NotFoundException {
        return chapterRepository.findById(id).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND.value(), "챕터 정보를 찾을 수 없습니다."));
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

    public List<Chapter> getChaptersByMemberId(Long myId, Long suggestId) throws NotFoundException {
        try {
            Suggest suggest = suggestService.getSuggestById(suggestId);
            GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(suggest.getGroup().getId(), myId);

            List<ChapterMember> chapterMembers = chapterRepository.findChaptersByMemberId(member.getId(), suggestId);

            List<Chapter> chapters = new ArrayList<>();
            for (ChapterMember chapterMember : chapterMembers) {
                chapters.add(chapterMember.getChapter());
            }

            return chapters;
        } catch (NotFoundException e) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "해당 챕터 정보를 찾을 수 없습니다.");
        }
    }

}
