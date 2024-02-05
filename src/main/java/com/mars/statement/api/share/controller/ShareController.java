package com.mars.statement.api.share.controller;


import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.share.dto.PersonalShareDto;
import com.mars.statement.api.share.dto.ShareDetailDto;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name="공유")
@RequestMapping("/api/v1/share")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService){
        this.shareService = shareService;
    }

    @Operation(summary = "공유 인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 인물별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonalShareDto.class)))})

    })
    @GetMapping("/personal/{suggestId}")
    public ResponseEntity<?> getPersonalShareDataList(@PathVariable Long suggestId) {
        Long myId = 1L; // 로그인 데이터
        List<PersonalShareDto> chapterDtoList = shareService.getPersonalShareData(suggestId, myId);

        return CommonResponse.createResponse(200, "공유 인물별 조회 성공", chapterDtoList);

    }
    @Operation(summary = "공유 회차별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 회차별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CheckChapterDto.class)))})
    })
    @GetMapping("/chapter/{suggestId}")
    public ResponseEntity<?> getChapterShareDataList( @PathVariable Long suggestId, @Parameter(hidden = true)UserDto userDto) {
        CheckChapterDto chapterDtoList = shareService.getChapterShareData(suggestId, userDto.getId());

        return CommonResponse.createResponse(200, "공유 회차별 조회 성공", chapterDtoList);

    }

    @Operation(summary = "공유 회차 디테일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 회차 디테일 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ShareDetailDto.class)))})
    })
    @GetMapping("/detail/{chapterId}")
    public ResponseEntity<?> getShareDetailData( @PathVariable Long chapterId, @Parameter(hidden = true)UserDto userDto) {
        ShareDetailDto shareDetails = shareService.getShareDetails(chapterId, userDto.getId());
        return CommonResponse.createResponse(200, "공유 회차별 조회 성공", shareDetails);
    }

}
