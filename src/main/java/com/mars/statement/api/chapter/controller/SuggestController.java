package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.service.CreateSuggestService;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mars.statement.global.dto.ExampleResponse.*;

@RestController
@Tag(name = "주제")
@RequestMapping("/api/v1/suggest")
@RequiredArgsConstructor
public class SuggestController {

    private final CreateSuggestService createSuggestService;

    @Operation(summary = "주제 생성")
    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주제 작성 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = SUCCESS_SUGGEST))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":403,\"status\":\"FORBIDDEN\",\"message\":\"생성자가 해당 주제를 소유한 그룹의 멤버가 아닙니다.\"}"))),
            @ApiResponse(responseCode = "404", description = "주제 또는 그룹을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"주제 또는 그룹을 찾을 수 없습니다.\"}")))
    })
    public ResponseEntity<?> createSuggest(@RequestBody SuggestDto suggestDto,@Parameter(hidden = true) UserDto userDto) throws Exception {
        return createSuggestService.createSuggest(suggestDto,userDto.getId());
    }
}