package com.mars.statement.api.share.controller;

import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.service.LikeService;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import com.mars.statement.global.exception.ForbiddenException;
import com.mars.statement.global.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mars.statement.global.dto.ExampleResponse.*;

@RestController
@Tag(name = "공유")
@RequiredArgsConstructor
@RequestMapping("/api/v1/share")
public class ShareController {

    private final ShareService shareService;
    private final LikeService likeService;

    @Operation(summary = "공유 인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 인물별 조회 성공 ",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_PERSONAL_SHARE))),
            @ApiResponse(responseCode = "404", description = "챕터 또는 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = NOT_FOUND_ERROR_RESPONSE))),
    })
    @GetMapping("/personal/{suggestId}")
    public ResponseEntity<?> getPersonalShareDataList(@PathVariable Long suggestId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        try {
            PersonalShareDto chapterDtoList = shareService.getPersonalShareData(suggestId, userDto.getId());
            return CommonResponse.createResponse(200, "공유 인물별 조회 성공", chapterDtoList);
        } catch (NotFoundException e) {
            return CommonResponse.createResponseMessage(404, "챕터 또는 멤버를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "공유 회차별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 회차별 조회 성공 ",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_CHAPTER_SHARE))),
            @ApiResponse(responseCode = "404", description = "챕터 또는 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = NOT_FOUND_ERROR_RESPONSE))),
    })
    @GetMapping("/chapter/{suggestId}")
    public ResponseEntity<?> getChapterShareDataList(@PathVariable Long suggestId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        try {
            CheckChapterDto chapterDtoList = shareService.getChapterShareData(suggestId, userDto.getId());
            return CommonResponse.createResponse(200, "공유 회차별 조회 성공", chapterDtoList);
        } catch (NotFoundException e) {
            return CommonResponse.createResponseMessage(404, "챕터 또는 멤버를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "공유 회차 디테일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 회차 디테일 조회 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_SHARE_DETAIL))),
            @ApiResponse(responseCode = "404", description = "챕터 또는 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = NOT_FOUND_ERROR_RESPONSE))),
    })
    @GetMapping("/detail/{chapterId}")
    public ResponseEntity<?> getShareDetailData(@PathVariable Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        try {
            ShareDetailDto shareDetails = shareService.getShareDetails(chapterId, userDto.getId());
            return CommonResponse.createResponse(200, "공유 회차 디테일 조회 성공", shareDetails);
        } catch (NotFoundException e) {
            return CommonResponse.createResponseMessage(404, "챕터 또는 멤버를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "공유 의견 좋아요 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 의견 좋아요 기능 성공 ",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_ADD_LIKE))),
            @ApiResponse(responseCode = "200", description = "공유 의견 좋아요 기능 취소 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_DEL_LIKE))),
            @ApiResponse(responseCode = "404", description = "챕터 또는 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = NOT_FOUND_ERROR_RESPONSE))),
    })
    @PostMapping("/detail/{chapterId}")
    public ResponseEntity<?> updateLike(@PathVariable Long chapterId, @RequestBody LikeRequest request, @Parameter(hidden = true) UserDto userDto) {
        try {
            int result = likeService.updateLike(chapterId, request.getShareId(), userDto.getId());

            if (result == 0) {
                return CommonResponse.createResponse(200, "공유 의견 좋아요 취소 성공", request);
            } else if (result == 1) {
                return CommonResponse.createResponse(200, "공유 의견 좋아요 성공", request);
            } else {
                return CommonResponse.createResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류");
            }
        } catch (NotFoundException e) {
            return CommonResponse.createResponseMessage(404, "챕터 또는 멤버를 찾을 수 없습니다: " + e.getMessage());
        }

    }
    @Operation(summary = "주제별 의견작성")
    @PostMapping("/write/{chapterId}")
    public ResponseEntity<?> insertShare(@PathVariable Long chapterId, @RequestBody ShareOpinionDto shareOpinionDto, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return shareService.insertShare(chapterId,shareOpinionDto,userDto.getId());
    }

    @Operation(summary = "서머리 작성")
    @PostMapping("/summary/{chapterId}")
    public ResponseEntity<?> summaryShare(@PathVariable Long chapterId, @RequestBody ShareSummaryDto shareSummaryDto, @Parameter(hidden = true) UserDto userDto) throws ForbiddenException {
        return shareService.summaryShare(chapterId,shareSummaryDto,userDto.getId());
    }
}