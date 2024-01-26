package com.mars.statement.global.controller;

import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MainController {
    @GetMapping("/")
    public ResponseEntity<?> mainP(UserDto userDto){
        return CommonResponse.createResponse(HttpStatus.OK.value(), "성공", userDto);
    }
}
