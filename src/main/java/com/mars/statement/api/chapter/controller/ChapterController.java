package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.service.CreateChapterService;
import com.mars.statement.api.share.dto.LikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "회차")
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final CreateChapterService createChapterService;

    @Operation(summary = "회차 작성")
    @PostMapping("/create")
    public ResponseEntity<?> createChapter(@RequestBody CreateChapterDto createChapterDto) throws Exception {
        return createChapterService.createChapterAndAddMembers(createChapterDto);
    }
}
