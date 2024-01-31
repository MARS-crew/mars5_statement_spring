package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.CreateChapterDto;
import com.mars.statement.api.chapter.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

@PostMapping("/create")
public ResponseEntity<?> createChapter(@RequestBody CreateChapterDto createChapterDto) throws Exception {
    return chapterService.createChapterAndAddMembers( createChapterDto);
}
}
