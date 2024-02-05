package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDTO;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.ForbiddenException;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mars.statement.api.chapter.domain.ChapterMember;


import java.util.ArrayList;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMemberService chapterMemberService;
    private final SuggestRepository suggestRepository;
    private final GroupMemberService groupMemberService;
    private final ModelMapper modelMapper;
    private final GroupMemberService groupMemberService;
    private final SuggestService suggestService;

    public Chapter findChapterById(Long id) {
            return chapterRepository.findById(id).orElse(null);
    }

    public ChapterWithMemberDTO getChapterWithMembers (Long chapter_id){
        Chapter chapter = chapterRepository.findChapterWithMembers(chapter_id);
            if (chapter != null) {
                List<ChapterMemberDTO> chapterMemberDTOList = chapter.getChapterMembers()
                        .stream()
                        .map(chapterMember -> new ChapterMemberDTO(
                                chapterMember.getGroupMember().getId(),
                                chapterMember.getSummary(),
                                chapterMember.getGroupMember().getUser().getName()
                            )).toList();
                return new ChapterWithMemberDTO(chapter.getId(), chapter.getSuggest().getSuggest(), chapter.getSuggest().getType(), chapterMemberDTOList);
            }

                return null;
    }
    @Transactional
    public ResponseEntity<?> createChapterAndAddMembers (CreateChapterDto createChapterDto) throws Exception {
        // 1. 주제 조회
        Suggest suggest = suggestRepository.findById(createChapterDto.getSuggestId())
                .orElseThrow(() -> new NotFoundException("Suggest not found"));

        // 2. 그룹 조회 (주제의 그룹)
        // 그룹 아이디로 주제를 소유한 그룹인지 판단
        GroupMember groupMember = groupMemberService.findGroupMemberById(createChapterDto.getConstructorId());
        Group group = suggest.getGroup();

        if (!groupMember.getGroup().equals(group)) {
            throw new ForbiddenException("Constructor is not a member of the group that owns the suggest.");
        }

        // 3. 회차 생성 및 그룹 설정
        Chapter chapter = Chapter.builder()
                .suggest(suggest)
                .group(group)
                .build();
        Chapter savedChapter = chapterRepository.save(chapter);

        // 4. 회차에 생성자와 멤버 추가
        chapterMemberService.addMemberToChapter(savedChapter.getId(), createChapterDto.getConstructorId(), createChapterDto.getMemberIds());

        return CommonResponse.createResponse(HttpStatus.OK.value(), "주제생성 완료", savedChapter.getId());
    }
    public List<Chapter> getChaptersByMemberId (Long group_id, Long my_id, Long suggest_id){

        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(group_id, my_id);
        Suggest suggest = suggestService.getSuggestById(suggest_id);

        List<ChapterMember> chapterMembers = chapterRepository.findChaptersByMemberId(member.getId(), suggest_id);

        List<Chapter> chapters = new ArrayList<>();
        for (ChapterMember chapterMember : chapterMembers) {
            chapters.add(chapterMember.getChapter());
        }
        return chapters;
    }

}