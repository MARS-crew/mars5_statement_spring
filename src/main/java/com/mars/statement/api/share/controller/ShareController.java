package com.mars.statement.api.share.controller;

import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.service.LikeService;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.SwaggerExampleValue;
import com.mars.statement.global.dto.UserDto;
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
    private final ChapterService chapterService;

    @Operation(summary = "공유 인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 인물별 조회 성공 ",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_PERSONAL_SHARE))),
            @ApiResponse(responseCode = "404", description = "챕터 또는 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = NOT_FOUND_ERROR_RESPONSE))),
    })
    @GetMapping("/personal/{suggestId}")
    public ResponseEntity<?> getPersonalShareDataList(@PathVariable("suggestId") Long suggestId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
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
    public ResponseEntity<?> getChapterShareDataList(@PathVariable("suggestId") Long suggestId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
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
    public ResponseEntity<?> getShareDetailData(@PathVariable("chapterId") Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
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
    public ResponseEntity<?> updateLike(@PathVariable("chapterId") Long chapterId, @RequestBody LikeRequest request, @Parameter(hidden = true) UserDto userDto) {
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 의견 작성 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value =SUCCESS_SHARE_WRITE))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":403,\"status\":\"FOR_BIDDEN\",\"message\":\"권한이 없습니다.\"}"))),
            @ApiResponse(responseCode = "404", description = "챕터 멤버를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"챕터 멤버를 찾을 수 없습니다.\"}"))),
    })
    @PostMapping("/write/{chapterId}")
    public ResponseEntity<?> insertShare(@PathVariable("chapterId") Long chapterId, @RequestBody ShareOpinionDto shareOpinionDto, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return shareService.insertShare(chapterId,shareOpinionDto,userDto.getId());
    }

    @Operation(summary = "서머리 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 서머리 작성 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value =SUCCESS_SHARE_SUMMARY))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":403,\"status\":\"FOR_BIDDEN\",\"message\":\"You are not authorized to share summary for this chapter.\"}"))),
    })
    @PostMapping("/summary/{chapterId}")
    public ResponseEntity<?> summaryShare(@PathVariable("chapterId") Long chapterId, @RequestBody ShareSummaryDto shareSummaryDto, @Parameter(hidden = true) UserDto userDto) throws Exception {
        return shareService.summaryShare(chapterId,shareSummaryDto,userDto.getId());
    }

    @Operation(summary = "Share 입장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 입장 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.JOIN_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "멤버가 회차에 속해있지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"User is not a member of this chapter\"}"))),
    })
    @PostMapping("/join/{chapterId}")
    public ResponseEntity<?> joinSend(@PathVariable("chapterId") Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return chapterService.join(chapterId, userDto);
    }

    @Operation(summary = "Share 입장 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 입장 확인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.GET_JOIN_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "멤버가 회차에 속해있지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"User is not a member of this chapter\"}"))),
    })
    @GetMapping("/join/{chapterId}")
    public ResponseEntity<?> getJoinSend(@PathVariable("chapterId") Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return chapterService.getJoin(chapterId, userDto);
    }

    @Operation(summary = "Share 작성 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 작성 확인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.GET_WRITE_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "멤버가 회차에 속해있지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"User is not a member of this chapter\"}"))),
    })
    @GetMapping("/write/{chapterId}")
    public ResponseEntity<?> getWriteSend(@PathVariable("chapterId") Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return chapterService.getWriteCnt(chapterId, userDto);
    }

    @Operation(summary = "Share 서머리 작성 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "share 서머리 확인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.GET_SUMMARY_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "멤버가 회차에 속해있지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"User is not a member of this chapter\"}"))),
    })
    @GetMapping("/summary/{chapterId}")
    public ResponseEntity<?> getSummaryBoolSend(@PathVariable("chapterId") Long chapterId, @Parameter(hidden = true) UserDto userDto) throws NotFoundException {
        return chapterService.getSummaryBool(chapterId, userDto);
    }
}