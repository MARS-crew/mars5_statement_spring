package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.dto.SuggestDto;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
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
    private final SuggestService suggestService;

/*    @PostMapping("/create")
    public ResponseEntity<Long> createChapter(@RequestBody SuggestDto suggestDto) {
        Long chapterId = chapterService.createChapterAndAddMembers( suggestDto.getGroupId(), suggestDto.getConstructorId());
        return ResponseEntity.ok(chapterId);
    }*/
@PostMapping("/create")
public ResponseEntity<Long> createChapter(@RequestBody SuggestDto suggestDto) {
    Long chapterId = suggestService.createSuggest(suggestDto);
    return ResponseEntity.ok(chapterId);
}
}
