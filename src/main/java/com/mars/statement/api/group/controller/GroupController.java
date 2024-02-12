package com.mars.statement.api.group.controller;

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


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/group")
@Tag(name = "Group", description = "그룹 API")

public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "메인페이지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 조회 성공", content =
            @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "메인페이지 조회 성공", value = SwaggerExampleValue.MAINPAGE_SUCCESS_RESPONSE),
                    @ExampleObject(name = "그룹이 없을 때", value = "{\"code\":200,\"status\":\"OK\",\"message\":\"속해있는 그룹이 없습니다.\"}")
            })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저 혹은 그룹입니다.", content =
            @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 유저", value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"존재하지 않는 유저입니다.\"}"),
                    @ExampleObject(name = "존재하지 않는 그룹", value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"존재하지 않는 그룹입니다.\"}")
            })),
            @ApiResponse(responseCode = "403", description = "해당 그룹에 접근 권한이 없습니다.", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":403,\"status\":\"FORBIDDEN\",\"message\":\"해당 그룹에 접근 권한이 없습니다.\"}")))
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<?> getMainPage( @PathVariable("groupId") Long groupId, @Parameter(hidden = true) UserDto userDto) throws Exception {
        return groupService.getMainPage(groupId, userDto);
    }

    @Operation(summary = "그룹 생성", description = "그룹 추가 및 멤버 추가(가입 회원이면 바로 추가 or 미가입 회원이면 초대 생성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.GROUP_CREATE_SUCCESS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"유저를 찾을 수 없습니다.\"}")))
    })
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateRequest request, @Parameter(hidden = true) UserDto userDto) throws Exception {
        return groupService.createGroup(request, userDto);
    }
}
