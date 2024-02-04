package com.mars.statement.api.share.controller;


import com.mars.statement.api.chapter.dto.ChapterMemberDto;
import com.mars.statement.api.share.dto.ChapterShareDto;
import com.mars.statement.api.share.dto.PersonalShareDto;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/share")

public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService){
        this.shareService = shareService;
    }

    @Tag(name="공유", description = "인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 인물별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonalShareDto.class)))})

    })
    @GetMapping("/personal/{groupId}/{suggestId}")
    public ResponseEntity<Object> getPersonalShareDataList(@PathVariable Long groupId, @PathVariable Long suggestId) {
        Long myId = 1L; // 로그인 데이터
        List<PersonalShareDto> chapterDtoList = shareService.getPersonalShareData(groupId,suggestId, myId);

        return CommonResponse.createResponse(200, "공유 인물별 조회 성공", chapterDtoList);

    }
    @Tag(name="공유", description = "회차별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 회차별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChapterMemberDto.class)))})

    })
    @GetMapping("/chapter/{groupId}/{suggestId}")
    public ResponseEntity<Object> getChapterShareDataList(@PathVariable Long groupId, @PathVariable Long suggestId) {
        Long myId = 1L; // 로그인 데이터
        ChapterShareDto chapterDtoList = shareService.getChapterShareData(groupId,suggestId, myId);

        return CommonResponse.createResponse(200, "공유 회차별 조회 성공", chapterDtoList);

    }
}
