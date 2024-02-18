package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDto;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDto;

import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final GroupMemberService groupMemberService;
    private final ChapterMemberRepository chapterMemberRepository;

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

    public List<Chapter> getChaptersByMemberId(Long myId, Suggest suggest) throws NotFoundException {
        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(suggest.getGroup().getId(), myId);

        List<ChapterMember> chapterMembers = chapterRepository.findChaptersByMemberId(member.getId(), suggest.getId());

        if(chapterMembers == null || chapterMembers.isEmpty()){
            throw  new NotFoundException(HttpStatus.NOT_FOUND.value(), "참여한 챕터가 없습니다.");
        }

        List<Chapter> chapters = new ArrayList<>();
        for (ChapterMember chapterMember : chapterMembers) {
            chapters.add(chapterMember.getChapter());
        }

        return chapters;
    }

    public ResponseEntity<?> join(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);
        chapter.increaseJoinCnt();
        chapterRepository.save(chapter);

        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 입장 성공");
    }

    public ResponseEntity<?> getJoin(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 입장 확인 성공", chapter.getJoinCnt());
    }

    public ResponseEntity<?> getWriteCnt(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 작성 확인 성공", chapter.getWriteCnt());
    }

    public ResponseEntity<?> getSummaryBool(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 서머리 작성 확인 성공", chapter.getSummaryBool());
    }

}
