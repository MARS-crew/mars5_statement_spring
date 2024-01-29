package com.mars.statement.api.send.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.MessageDTO;
import com.mars.statement.api.send.repository.SendRepository;
import com.mars.statement.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SendService {

    private final SendRepository sendRepository;
    private final ChapterMemberService chapterMemberService;
    private final ChapterService chapterService;

    private final ModelMapper modelMapper;


    @Autowired
    public SendService(SendRepository sendRepository, ModelMapper modelMapper,
                       ChapterService chapterService, ChapterMemberService chapterMemberService){
        this.sendRepository = sendRepository;
        this.modelMapper = modelMapper;
        this.chapterService = chapterService;
        this.chapterMemberService = chapterMemberService;
    }

    @Transactional
    public int saveSendMessage(Long chapter_id, List<MessageDTO> messageDTOList, Long from_id){

        Chapter chapter = chapterService.findChapterById(chapter_id);
        ChapterMember from = chapterMemberService.findChapterMemberById(chapter.getId(), from_id);
        chapter.setId(chapter_id);
        from.setId(from_id);

        List<Send> sendList = new ArrayList<>();

        for(MessageDTO messageDTO : messageDTOList){
            ChapterMember to = chapterMemberService.findChapterMemberById(chapter.getId(), messageDTO.getTo_id());

            Send send = new Send(chapter, from, to, messageDTO.getMessage());



            sendList.add(send);
        };

        List<Send> savedSends = sendRepository.saveAll(sendList);

        for(Send savedSend : savedSends){
           if(savedSend.getId() == null){
               System.out.println("메세지 저장 실패");
               return -1;
           }
        }

        return 0;
    }

}
