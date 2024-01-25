package com.mars.statement.api.send.controller;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.service.ChapterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/send")
public class SendController {

    private final ChapterService chapterService;

    @Autowired
    public SendController(ChapterService chapterService){
        this.chapterService = chapterService;
    }


    @Tag(name="전달", description = "회차멤버 조회")
    @GetMapping("/{chapterId}")
    public ResponseEntity<ChapterWithMemberDTO> getChapterWithMembers(@PathVariable Long chapterId){
        ChapterWithMemberDTO chapterDTO = chapterService.getChapterWithMembers(chapterId);

        return ResponseEntity.ok(chapterDTO);
    }
}
