package com.mars.statement.api.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.send.dto.MessageDTO;
import com.mars.statement.api.send.service.SendService;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/send")
public class SendController {

    private final ChapterService chapterService;
    private final SendService sendService;


    public SendController(ChapterService chapterService, SendService sendService) {
        this.chapterService = chapterService;
        this.sendService = sendService;
    }


    @Tag(name = "전달", description = "회차멤버 조회")
    @GetMapping("/{chapter_id}")
    public ResponseEntity<Object> getChapterWithMembers(@PathVariable Long chapter_id) throws JsonProcessingException {
        ChapterWithMemberDTO chapter = chapterService.getChapterWithMembers(chapter_id);

        return CommonResponse.createResponse(200, "회차 멤버 조회 성공", chapter);
    }

    @Tag(name = "전달", description = "메세진 작성")
    @PostMapping("/write/{chapter_id}")
    public ResponseEntity<Object> writeMessage(@PathVariable Long chapter_id, @RequestBody List<MessageDTO> messageDTOList) {

        int code;
        String message;

        if (messageDTOList == null) {
            code = 400;
            message = "메세지 전달 실패: 요청 데이터 null";
        } else {
            Long to_id = 1L; // 로그인 데이터
            int result = sendService.saveSendMessage(chapter_id, messageDTOList, to_id);
            if (result == 0) {
                code = 200;
                message = "메세지 전달 성공";
            } else {
                code = 500;
                message = (result == -1) ? "메세지 전달 실패: 데이터 저장 오류" : "메세지 전달 실패: 기타 원인";

            }
        }

        return CommonResponse.createResponseMessage(code, message);
    }

}
