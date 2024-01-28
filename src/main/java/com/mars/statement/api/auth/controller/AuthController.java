package com.mars.statement.api.auth.controller;

import com.mars.statement.api.auth.dto.LoginRequest;
import com.mars.statement.api.auth.service.AuthService;
import com.mars.statement.global.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    // 로그인 테스트 url(code생성) https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email&client_id=871110799541-37dbm96v47dr7pdvi1a8l3nr8slp6n5t.apps.googleusercontent.com&response_type=code&redirect_uri=http://localhost:8080/google-login&access_type=offline
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws NotFoundException {
        return authService.doSocialLogin(loginRequest);
    }


}
