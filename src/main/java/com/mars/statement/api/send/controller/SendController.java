package com.mars.statement.api.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDto;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.PersonalSendDto;
import com.mars.statement.api.send.dto.SendDetailDto;
import com.mars.statement.api.send.dto.SendMessageDto;
import com.mars.statement.api.send.service.SendService;

import com.mars.statement.api.share.dto.ShareDetailDto;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "전달")
@RequiredArgsConstructor
@RequestMapping("/api/v1/send")
public class SendController {

    private final ChapterService chapterService;
    private final SendService sendService;


    @GetMapping("/{chapterId}")
    public ResponseEntity<Object> getChapterWithMembers(@PathVariable Long chapterId) throws JsonProcessingException {
        ChapterWithMemberDto chapter = chapterService.getChapterWithMembers(chapterId);


        return CommonResponse.createResponse(200, "회차 멤버 조회 성공", chapter);
    }

    @Operation(summary = "메세지 작성")
    @PostMapping("/write/{chapterId}")
    public ResponseEntity<Object> writeMessage(@PathVariable Long chapterId, @RequestBody List<SendMessageDto> messageDtoList) {


        int code;
        String message;

        if (messageDtoList == null) {
            code = 400;
            message = "메세지 전달 실패: 요청 데이터 null";
        } else {
            Long toId = 1L; // 로그인 데이터
            int result = sendService.saveSendMessage(chapterId, messageDtoList, toId);

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

    @Operation(summary = "전달 인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전달 인물별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonalSendDto.class)))})
    })
    @GetMapping("/personal/{groupId}/{suggestId}")
    public ResponseEntity<?> getPersonalSendDataList(@PathVariable Long groupId, @PathVariable Long suggestId) {
        Long myId = 3L; // 로그인 데이터
        List<PersonalSendDto> personalSendDataList = sendService.getPersonalSendData(groupId, suggestId, myId);

        return CommonResponse.createResponse(200, "전달 인물별 조회 성공", personalSendDataList);
    }
    @Operation(summary = "전달 회차별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="전달 회차별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CheckChapterDto.class)))})
    })
    @GetMapping("/chapter/{suggestId}")
    public ResponseEntity<?> getChapterSendDataList( @PathVariable Long suggestId, @Parameter(hidden = true) UserDto userDto) {
        CheckChapterDto chapterDtoList = sendService.getChapterSendData(suggestId, userDto.getId());

        return CommonResponse.createResponse(200, "전달 회차별 조회 성공", chapterDtoList);

    }
    @Operation(summary = "전달 회차 디테일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="전달 회차 디테일 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SendDetailDto.class)))})
    })
    @GetMapping("/detail/{chapterId}")
    public ResponseEntity<?> getSendDetailData( @PathVariable Long chapterId, @Parameter(hidden = true)UserDto userDto) {
        SendDetailDto sendDetails = sendService.getSendDetails(chapterId, userDto.getId());
        return CommonResponse.createResponse(200, "공유 회차별 조회 성공", sendDetails);
    }
    @Operation(summary = "전달 북마크 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="전달 북마크 처리 성공 ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(type = "integer"))})
    })
    @GetMapping("/bookmark/{sendId}")
    public ResponseEntity<?> updateBookmark(@PathVariable Long sendId, @Parameter(hidden = true) UserDto userDto) {
        try {
            int result = sendService.updateBookmark(sendId, userDto.getId());
            return CommonResponse.createResponse(200, "전달 북마크 처리 성공", sendId);
        } catch (NotFoundException e) {
            return CommonResponse.createResponseMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
