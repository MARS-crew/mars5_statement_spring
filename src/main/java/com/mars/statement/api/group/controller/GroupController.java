package com.mars.statement.api.group.controller;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.api.group.dto.GroupCreateRequest;
import com.mars.statement.api.group.service.GroupService;
import com.mars.statement.global.dto.SwaggerExampleValue;
import com.mars.statement.global.dto.UserDto;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/group")
@Tag(name = "Group", description = "그룹 API")
public class GroupController {
    private final GroupService groupService;
    private final SuggestService suggestService;

    @Operation(summary = "그룹 주제 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="그룹 조회 성공 ",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Suggest.class)))})
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<Object> getGroupSuggest(@PathVariable Long groupId) {
        List<Suggest> suggest = suggestService.getSuggestByGroupId(groupId);

        return CommonResponse.createResponse(200, "그룹 주제 조회 성공", suggest);
    }

    @Operation(summary = "그룹 생성", description = "그룹 추가 및 멤버 추가(가입 회원이면 바로 추가 or 미가입 회원이면 초대 생성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.GROUP_CREATE_SUCCESS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"유저를 찾을 수 없습니다.\"}")))
    })
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateRequest request,@Parameter(hidden = true) UserDto userDto) throws Exception {
        return groupService.createGroup(request, userDto);
    }
}
