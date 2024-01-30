package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.service.SuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggests")
@RequiredArgsConstructor
public class SuggestController {

    private final SuggestService suggestService;

    @PostMapping
    public ResponseEntity<Long> createSuggest(@RequestBody SuggestDto suggestDto) {
        Long suggestId = suggestService.createSuggest(suggestDto);
        return ResponseEntity.ok(suggestId);
    }
}