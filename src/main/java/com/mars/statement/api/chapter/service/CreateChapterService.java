package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.ForbiddenException;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateChapterService {
    private final ChapterRepository chapterRepository;

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;

    private final ChapterMemberService chapterMemberService;

    private final SuggestRepository suggestRepository;
    @Transactional
    public ResponseEntity<?> createChapterAndAddMembers(CreateChapterDto createChapterDto,Long myId) throws Exception {
        // 1. 주제 조회
        Suggest suggest = suggestRepository.findById(createChapterDto.getSuggestId())
                .orElseThrow(() -> new NotFoundException(404, "Suggest not found"));

        // 2. 그룹 조회 (주제의 그룹)
        // 그룹 아이디로 주제를 소유한 그룹인지 판단
        Group group = suggest.getGroup();

        // 3. 현재 사용자가 주어진 그룹의 멤버인지 확인
        GroupMember groupMember = groupMemberService.findGroupMemberByIdAndGroupId(myId, group.getId());

        if (!groupMember.getGroup().getId().equals(group.getId())) {
            throw new ForbiddenException("Constructor is not a member of the group that owns the suggest.");
        }


        // 3. 회차 생성 및 그룹 설정
        Chapter chapter = Chapter.builder()
                .suggest(suggest)
                .build();
        Chapter savedChapter = chapterRepository.save(chapter);

        // 4. 회차에 생성자와 멤버 추가
        chapterMemberService.addMemberToChapter(savedChapter.getId(), myId, createChapterDto.getMemberIds());

        return CommonResponse.createResponse(HttpStatus.OK.value(), "주제생성 완료", savedChapter.getId());
    }
}
