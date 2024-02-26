package com.mars.statement.api.send.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterSummaryDto;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.*;
import com.mars.statement.api.send.repository.SendRepository;
import com.mars.statement.api.share.dto.ShareDetailDto;
import com.mars.statement.api.share.dto.ShareMemberDetailDto;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final ChapterMemberService chapterMemberService;
    private final GroupMemberService groupMemberService;
    private final SuggestService suggestService;
    private final ChapterService chapterService;
    private final ChapterRepository chapterRepository;
    private final ChapterMemberRepository chapterMemberRepository;

    private final SendRepository sendRepository;

    @Transactional
    public ResponseEntity<?> saveSendMessage(Long chapterId, List<SendMessageDto> messageDtoList, Long myId) throws NotFoundException {
        Chapter chapter = chapterService.getChapterById(chapterId);
//            ChapterMember from = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(), myId);

        // 확인: 보내는 멤버가 챕터에 속해 있는지 확인
        Optional<ChapterMember> myChapterMember = Optional.ofNullable(chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId));
        ChapterMember from = myChapterMember.orElseThrow(() -> new NotFoundException(404, "보내는 멤버가 챕터에 속해 있지 않습니다."));

        System.out.println("from: " + from.getId());
        List<Send> sendList = new ArrayList<>();

        for (SendMessageDto messageDto : messageDtoList) {
            Long toId = messageDto.getTo_id();

            // 확인: 받는 멤버가 챕터에 속해 있는지 확인
            Optional<ChapterMember> checkChapterMember = Optional.ofNullable(chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, toId));
            ChapterMember to = checkChapterMember.orElseThrow(() -> new NotFoundException(404, "받는 멤버가 챕터에 속해 있지 않습니다."));
            System.out.println("to: " + to.getId());

            Send send = Send.builder()
                    .chapter(chapter)
                    .from(from)
                    .to(to)
                    .message(messageDto.getMessage())
                    .location(messageDto.getLocation())
                    .build();
            sendList.add(send);
        }


        List<Send> savedSends = sendRepository.saveAll(sendList);

        for (Send savedSend : savedSends) {
            if (savedSend.getId() == null) {
                System.out.println("메세지 저장 실패");
                return CommonResponse.createResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "메세지 작성 성공");
            }
        }

        chapter.increaseWriteCnt();
        chapterRepository.save(chapter);

        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "메세지 작성 성공");

    }

    public PersonalSendDto getPersonalSendData(Long suggestId, Long myId) throws NotFoundException {
        Suggest suggest = suggestService.getSuggestById(suggestId);
        // 챕터 조회
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggest);
        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(suggest.getGroup().getId(), myId);

        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        // 리스트
        List<PersonalSendDto> personalSends = sendRepository.findPersonalSharesByIds(chapterIds, member.getId());

        if (personalSends.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "해당 제안 정보를 찾을 수 없습니다.");
        }

        List<MemberMessageDto> allMemberMessageList = personalSends.stream()
                .collect(Collectors.groupingBy(
                        personalSendDto -> personalSendDto.getMemberMessageDto().getMemberId(),
                        Collectors.mapping(
                                PersonalSendDto::getMessageList,
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(memberEntry -> {
                    Long memberId = memberEntry.getKey();
                    List<MessageDto> messageList = memberEntry.getValue().stream()
                            .flatMap(List::stream)
                            .flatMap(dto -> dto.getMessageList().stream())
                            .toList();

                    MemberMessageDto memberMessageDto = personalSends.stream()
                            .filter(dto -> dto.getMemberMessageDto().getMemberId().equals(memberId))
                            .findFirst()
                            .map(PersonalSendDto::getMemberMessageDto)
                            .orElseThrow();

                    return new MemberMessageDto(memberMessageDto.getMemberId(), memberMessageDto.getMemberName(),
                            memberMessageDto.getMemberImg(), messageList);
                })
                .toList();
        return new PersonalSendDto(personalSends.get(0).getSuggestId(), personalSends.get(0).getSuggest(), allMemberMessageList);
    }

    public CheckChapterDto getChapterSendData(Long suggestId, Long myId) throws NotFoundException {
        Suggest suggest = suggestService.getSuggestById(suggestId);
        if (!suggest.getType().equals("send")) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "전달 주제가 아닙니다.");
        }
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggest);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<CheckChapterDto> checkChapterDtoList = sendRepository.findChapterSendsByIds(chapterIds, myId);

        List<ChapterSummaryDto> allChapterSummaryDtoList = new ArrayList<>();

        for (CheckChapterDto checkChapterDto : checkChapterDtoList) {
            allChapterSummaryDtoList.add(checkChapterDto.getChapterSummaryDto());
        }
        return new CheckChapterDto(checkChapterDtoList.get(0).getSuggestId(), checkChapterDtoList.get(0).getSuggest(), allChapterSummaryDtoList);
    }

    public SendDetailDto getSendDetails(Long chapterId, Long myId) throws NotFoundException {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(), myId);

        List<SendDetailDto> sendDetailList = sendRepository.findSendDetails(chapter.getId(), member.getId());

        List<SendMemberDetailDto> allChapterMemberDetailList = new ArrayList<>();
        for (SendDetailDto sendDetailDto : sendDetailList) {
            allChapterMemberDetailList.add(sendDetailDto.getSendMemberDetailDto());
        }
        return new SendDetailDto(sendDetailList.get(0).getSuggestId(), sendDetailList.get(0).getSuggest(),
                sendDetailList.get(0).getSeq(), sendDetailList.get(0).getChapterId(), sendDetailList.get(0).getRegDt(), sendDetailList.get(0).getSummary(),
                allChapterMemberDetailList);

    }

    public Send getSendById(Long sendId) throws NotFoundException {
        return sendRepository.findById(sendId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "전달 정보를 찾을 수 없습니다."));
    }

    public int updateBookmark(Long sendId, Long myId) throws NotFoundException {

        Send send = getSendById(sendId);

        return sendRepository.updateBookmark(send.getId());


    }

    @Transactional
    public ResponseEntity<?> summarySend(Long chapterId, SendSummaryDto sendSummaryDto, Long myId) throws NotFoundException {
        String summary = sendSummaryDto.getSummary();

        // 주어진 챕터의 멤버인지 확인
        ChapterMember chapterMember = chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId);
        if (chapterMember == null) {
            throw new NotFoundException(404, "User is not a member of this chapter");
        }

        // 요약 정보 업데이트
        chapterMember.withSummary(summary);
        chapterMemberRepository.save(chapterMember);

        Chapter chapter = chapterService.getChapterById(chapterId);
        chapter.changeSummaryBool();
        chapterRepository.save(chapter);

        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "서머리 작성 성공");
    }
}
