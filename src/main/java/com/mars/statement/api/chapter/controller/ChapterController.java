package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.service.CreateChapterService;
import com.mars.statement.global.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mars.statement.global.dto.ExampleResponse.*;

@RestController
@Tag(name = "회차")
@RequestMapping("/api/v1/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final CreateChapterService createChapterService;

    @Operation(summary = "회차 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회차 생성 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_CHAPTER))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":403,\"status\":\"FORBIDDEN\",\"message\":\"생성자가 해당 주제를 소유한 그룹의 멤버가 아닙니다.\"}"))),
            @ApiResponse(responseCode = "404", description = "주제 또는 그룹을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"주제 또는 그룹을 찾을 수 없습니다.\"}")))
    })
    @PostMapping("/create/{suggestId}")
    public ResponseEntity<?> createChapter(@PathVariable("suggestId") Long suggestId, @RequestBody CreateChapterDto createChapterDto, @Parameter(hidden = true) UserDto userDto) throws Exception {
        List<Long> memberIds = createChapterDto.getMemberIds();
        return createChapterService.createChapterAndAddMembers(suggestId, userDto.getId(), memberIds);
    }

}