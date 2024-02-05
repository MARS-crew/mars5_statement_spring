package com.mars.statement.api.send.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.ChapterSummaryDto;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
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
import com.mars.statement.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendService {

    private final ChapterMemberService chapterMemberService;
    private final GroupMemberService groupMemberService;
    private final SuggestService suggestService;
    private final ChapterService chapterService;

    private final SendRepository sendRepository;

    @Transactional
    public int saveSendMessage(Long chapterId, List<SendMessageDto> messageDtoList, Long fromId) {

        try {
            Chapter chapter = chapterService.getChapterById(chapterId);
            ChapterMember from = chapterMemberService.getChapterMemberById(chapter.getId(), fromId);

            List<Send> sendList = new ArrayList<>();

            for (SendMessageDto messageDto : messageDtoList) {
                ChapterMember to = chapterMemberService.getChapterMemberById(chapter.getId(), messageDto.getTo_id());
                System.out.println(to.getId());
                Send send = new Send(chapter, from, to, messageDto.getMessage());
                sendList.add(send);
            }

            List<Send> savedSends = sendRepository.saveAll(sendList);

            for (Send savedSend : savedSends) {
                if (savedSend.getId() == null) {
                    System.out.println("메세지 저장 실패");
                    return -1;
                }
            }

            return 0;
        } catch (Exception e) {
            throw new RuntimeException("메세지 저장 중에 오류가 발생했습니다.", e);
        }
    }

    public List<PersonalSendDto> getPersonalSendData(Long groupId, Long suggestId, Long myId) {

        // 챕터 조회
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);

        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(groupId, myId);

        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        // 리스트
        List<PersonalSendDto> personalSends = sendRepository.findPersonalSharesByIds(chapterIds, member.getId());

        return personalSends.stream()
                .collect(Collectors.groupingBy(
                        PersonalSendDto::getSuggestId,
                        Collectors.groupingBy(
                                personalSendDto -> personalSendDto.getMessageList().get(0).getMemberId(),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().entrySet().stream()
                        .map(memberEntry -> {
                            Long sId = suggestEntry.getKey();
                            Long memberId = memberEntry.getKey();
                            List<PersonalSendDto> personalSendDto = memberEntry.getValue();

                            List<MessageDto> messageDtoList = personalSendDto.stream()
                                    .flatMap(dto -> dto.getMessageList().stream())
                                    .map(MemberMessageDto::getMessageList)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());

                            MemberMessageDto mergedMemberMessageDto = new MemberMessageDto(
                                    memberId,
                                    personalSendDto.get(0).getMessageList().get(0).getMemberName(),
                                    personalSendDto.get(0).getMessageList().get(0).getMemberImg(),
                                    messageDtoList
                            );
                            return new PersonalSendDto(sId, personalSendDto.get(0).getSuggest(), mergedMemberMessageDto);
                        }))
                .toList();

    }

    public CheckChapterDto getChapterSendData(Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<CheckChapterDto> checkChapterDtoList = sendRepository.findChapterSendsByIds(chapterIds, myId);

        List<ChapterSummaryDto> allChapterSummaryDtoList = new ArrayList<>();

        for (CheckChapterDto checkChapterDto : checkChapterDtoList) {
            allChapterSummaryDtoList.add(checkChapterDto.getChapterSummaryDto());
        }
        return new CheckChapterDto(checkChapterDtoList.get(0).getSuggestId(), checkChapterDtoList.get(0).getSuggest(), allChapterSummaryDtoList);
    }

    public SendDetailDto getSendDetails(Long chapterId, Long myId) {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(), myId);

        List<SendDetailDto> sendDetailList = sendRepository.findSendDetails(chapter.getId(), member.getId());

        List<SendMemberDetailDto> allChapterMemberDetailList = new ArrayList<>();
        for (SendDetailDto sendDetailDto : sendDetailList) {
            allChapterMemberDetailList.add(sendDetailDto.getSendMemberDetailDto());
        }
        return new SendDetailDto(sendDetailList.get(0).getSuggestId(), sendDetailList.get(0).getSuggest(),
                sendDetailList.get(0).getChapterId(), sendDetailList.get(0).getSummary(),
                allChapterMemberDetailList);

    }

    public Send getSendById(Long sendId) throws NotFoundException {
        return sendRepository.findById(sendId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "전달 정보를 찾을 수 없습니다."));
    }

    public int updateBookmark(Long sendId, Long myId) throws NotFoundException {

        Send send = getSendById(sendId);

        System.out.println(send.getId()+ " , bool" + send.getBookmark());

        return sendRepository.updateBookmark(send.getId());


    }


}
