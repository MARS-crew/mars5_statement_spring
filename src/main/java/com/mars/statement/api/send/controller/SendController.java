package com.mars.statement.api.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDto;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.send.dto.PersonalSendDto;
import com.mars.statement.api.send.dto.SendMessageDto;
import com.mars.statement.api.send.service.SendService;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/send")
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
        ChapterWithMemberDto chapter = chapterService.getChapterWithMembers(chapter_id);

        return CommonResponse.createResponse(200, "회차 멤버 조회 성공", chapter);
    }

    @Tag(name = "전달", description = "메세진 작성")
    @PostMapping("/write/{chapter_id}")
    public ResponseEntity<Object> writeMessage(@PathVariable Long chapter_id, @RequestBody List<SendMessageDto> messageDtoList) {

        int code;
        String message;

        if (messageDtoList == null) {
            code = 400;
            message = "메세지 전달 실패: 요청 데이터 null";
        } else {
            Long to_id = 1L; // 로그인 데이터
            int result = sendService.saveSendMessage(chapter_id, messageDtoList, to_id);
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

    @Tag(name="전달", description = "인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="전달 인물별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonalSendDto.class)))})
    })
    @GetMapping("/{group_id}/{suggest_id}")
    public ResponseEntity<Object> getPersonalSendDatas(@PathVariable Long group_id, @PathVariable Long suggest_id) {
        Long my_id = 3L; // 로그인 데이터
        List<PersonalSendDto> personalSendList = sendService.getPersonalSendData(group_id,suggest_id, my_id);

        return CommonResponse.createResponse(200, "전달 인물별 조회 성공", personalSendList);
    }



}
