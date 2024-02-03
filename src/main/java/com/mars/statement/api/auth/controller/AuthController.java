package com.mars.statement.api.auth.controller;

import com.mars.statement.api.auth.dto.LoginRequest;
import com.mars.statement.api.auth.dto.TokenReissueRequest;
import com.mars.statement.api.auth.service.AuthService;
import com.mars.statement.global.dto.SwaggerExampleValue;
import com.mars.statement.global.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인", description = "계정 정보가 없다면 회원가입, 그룹 초대 수락 후 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.LOGIN_SUCCESS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":404,\"status\":\"NOT_FOUND\",\"message\":\"유저를 찾을 수 없습니다.\"}")))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws NotFoundException {
        return authService.login(loginRequest);
    }

    @Operation(summary = "토큰 재발급", description = "access-token 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = SwaggerExampleValue.REISSUE_SUCCESS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "refresh token이 만료되었습니다.", content =
            @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":400,\"status\":\"BAD_REQUEST\",\"message\":\"refresh token이 만료되었습니다.\"}")))
    })
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody @Valid TokenReissueRequest request){
        return authService.reissueToken(request);
    }


}
