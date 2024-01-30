package com.mars.statement.api.chapter.controller;

import com.mars.statement.api.chapter.service.ChapterMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chaptermembers")
@RequiredArgsConstructor
public class ChapterMemberController {

    private final ChapterMemberService chapterMemberService;

    @PostMapping("/add")
    public ResponseEntity<?> addMemberToChapter(@RequestParam Long chapterId, @RequestParam Long memberId) {
        chapterMemberService.addMemberToChapter(chapterId, memberId);
        return ResponseEntity.ok().build();
    }
}

