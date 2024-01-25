package com.mars.statement.api.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.global.dto.ApiResponse;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getChapterWithMembers(@PathVariable Long chapterId) throws JsonProcessingException {
        ChapterWithMemberDTO chapter  = chapterService.getChapterWithMembers(chapterId);


        return CommonResponse.createResponse(200,"회차 멤버 조회 성공", chapter);
    }

//    @Tag(name="전달", description = "메세진 작성")
//    @PostMapping("/write")
//    public ResponseEntity

}
