package com.mars.statement.api.chapter.service;

import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.*;

import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.repository.SendRepository;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final GroupMemberService groupMemberService;
    private final ChapterMemberRepository chapterMemberRepository;
    private final ShareRepository shareRepository;
    private final SendRepository sendRepository;

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

        List<ChapterMember> members = chapterMemberRepository.findByChapter(chapter);
        List<ChapterJoinDto> chapterJoinDtos = new ArrayList<>();
        for (ChapterMember member : members) {
            ChapterJoinDto data = ChapterJoinDto.builder()
                    .userId(member.getGroupMember().getUser().getId())
                    .name(member.getGroupMember().getUser().getName())
                    .img(member.getGroupMember().getUser().getImg())
                    .build();
            chapterJoinDtos.add(data);
        }
        System.out.println(members);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 입장 성공", chapterJoinDtos);
    }

    public ResponseEntity<?> getJoin(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 입장 확인 성공",
                GetJoinCntDto.builder()
                        .joinCnt(chapter.getJoinCnt())
                        .memberCnt(chapter.getMemberCnt())
                        .build());
    }

    public ResponseEntity<?> getWriteCnt(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);

        return CommonResponse.createResponse(HttpStatus.OK.value(), chapter.getSuggest().getType() + " 작성 확인 성공",
                GetWriteCntDto.builder()
                        .writeCnt(chapter.getWriteCnt())
                        .memberCnt(chapter.getMemberCnt())
                        .build());
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

    public ResponseEntity<?> getShareOpinion(Long chapterId, UserDto userDto) throws NotFoundException {
        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }
        List<GetOpinionDto> getOpinionDtos = new ArrayList<>();
        Chapter chapter = getChapterById(chapterId);
        List<ChapterMember> members = chapterMemberRepository.findByChapter(chapter);
        List<Share> shares = new ArrayList<>();
        Long constructorId = 0l;
        for (ChapterMember member : members) {
            shares.add(shareRepository.findByChapterMember(member));
            if (member.getIs_constructor()){
                constructorId = member.getGroupMember().getUser().getId();
            }
        }
        for (Share share : shares) {
            getOpinionDtos.add(GetOpinionDto.builder()
                    .userId(share.getChapterMember().getGroupMember().getUser().getId())
                    .opinion(share.getOpinion()).build());
        }

        return CommonResponse.createResponse(HttpStatus.OK.value(), "조회 성공",
                OpinionResDto.builder().opinions(getOpinionDtos).constructorId(constructorId).build());

    }
    public ResponseEntity<?> getSendMessage(Long chapterId, UserDto userDto) throws NotFoundException {
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, userDto.getId());
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        Chapter chapter = getChapterById(chapterId);
        List<GetMessageDto> getMessageDtos = new ArrayList<>();
        List<Send> sends = sendRepository.findByToAndChapter(chapterMember, chapter);
        for (Send send : sends) {
            getMessageDtos.add(GetMessageDto.builder()
                    .userId(send.getFrom().getGroupMember().getUser().getId())
                    .message(send.getMessage()).build());
        }

        return CommonResponse.createResponse(HttpStatus.OK.value(), "조회 성공", MessageResDto.builder().messages(getMessageDtos).build());

    }
}
