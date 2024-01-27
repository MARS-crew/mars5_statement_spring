package com.mars.statement.api.share.controller;

import com.mars.statement.api.share.dto.ShareDto;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/share")
public class ShareController {

    private final ShareService shareService;

    @PostMapping
    public ResponseEntity<?> insertShare(@RequestBody ShareDto shareDto) {
        try {
            return shareService.insertShare(shareDto);
        } catch (Exception e) {
            // 예외 처리 로직 추가
            return CommonResponse.createResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Share 생성 실패");
        }
    }
}