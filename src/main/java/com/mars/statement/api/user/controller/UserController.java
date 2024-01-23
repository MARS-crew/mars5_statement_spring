package com.mars.statement.api.user.controller;

import com.mars.statement.api.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDTO){
        try {
            userService.join(joinDTO);
            // 회원가입 성공 시 처리
        } catch (RuntimeException e) {
            // 중복된 사용자명 예외 처리
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok().body("회원가입 성공.");
    }*/
}
