package com.mars.statement.api.send.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.*;
import com.mars.statement.api.send.repository.SendRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SendService {

    private final SendRepository sendRepository;
    private final ChapterMemberService chapterMemberService;

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ChapterService chapterService;


    @Autowired
    public SendService(SendRepository sendRepository,
                       ChapterService chapterService, ChapterMemberService chapterMemberService, GroupMemberService groupMemberService, SuggestService suggestService) {
        this.sendRepository = sendRepository;
        this.chapterService = chapterService;
        this.chapterMemberService = chapterMemberService;
        this.groupMemberService = groupMemberService;
        this.suggestService = suggestService;
    }


    @Transactional
    public int saveSendMessage(Long chapter_id, List<SendMessageDto> messageDtoList, Long from_id) {

        try {
            Chapter chapter = chapterService.findChapterById(chapter_id);
            ChapterMember from = chapterMemberService.findChapterMemberById(chapter.getId(), from_id);
            System.out.println(chapter.getId()+" "+ from.getId());

            List<Send> sendList = new ArrayList<>();

            for (SendMessageDto messageDto : messageDtoList) {
                ChapterMember to = chapterMemberService.findChapterMemberById(chapter.getId(), messageDto.getTo_id());
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

    public List<PersonalSendDto> getPersonalSendData(Long groupId, Long suggestId, Long myId){

        GroupMember member = groupMemberService.getGroupMemberByGroupIdAndUser(groupId,myId);


        // 챕터 조회
        List<Chapter> chapters = chapterService.getChaptersByMemberId(groupId, myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();
        // 리스트
        System.out.println(chapterIds);
        List<PersonalSendDto> personalSends = sendRepository.findPersonalSharesByIds(chapterIds, member.getId());


        Map<Long, Map<Long, List<PersonalSendDto>>> groupedSends = personalSends.stream()
                .collect(Collectors.groupingBy(
                        PersonalSendDto:: getSuggestId,
                        Collectors.groupingBy(
                                sendDto-> sendDto.getMemberMessageDtoList().get(0).getMemberId()
                        )
                ));



        return groupedSends.entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().values().stream()
                        .map(sendDtoS -> {
                            Long sId = suggestEntry.getKey();

                            List<MemberMessageDto> memberMessageList = sendDtoS.stream()
                                    .map(PersonalSendDto::getMemberMessageDtoList)
                                    .flatMap(List::stream)
                                    .toList();

                            return new PersonalSendDto(suggestId, sendDtoS.get(0).getSuggest(), memberMessageList);
                        }))
                .toList();

    }

}