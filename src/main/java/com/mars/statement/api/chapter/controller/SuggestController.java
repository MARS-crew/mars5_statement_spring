package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.service.CreateSuggestService;
import com.mars.statement.api.chapter.service.SuggestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "주제")
@RequestMapping("/api/v1/suggest")
@RequiredArgsConstructor
public class SuggestController {

    private final CreateSuggestService createSuggestService;

    @Operation(summary = "주제 작성")
    @PostMapping
    public ResponseEntity<?> createSuggest(@RequestBody SuggestDto suggestDto) throws Exception {
        return createSuggestService.createSuggest(suggestDto);
    }
}