package com.mars.statement.api.share.controller;

import com.mars.statement.api.share.dto.ShareDTO;
import com.mars.statement.api.share.dto.ShareDto;
import com.mars.statement.api.share.service.ShareService;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
            return CommonResponse.createResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "의견 작성 실패");
        }
    }

    @Tag(name="공유", description = "인물별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="공유 인물별 조회 성공 ",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ShareDTO.class)))})
    })
    @GetMapping("/{group_id}/{suggest_id}")
    public ResponseEntity<Object> getPersonalShareDatas(@PathVariable Long group_id, @PathVariable Long suggest_id) {
        Long my_id = 1L; // 로그인 데이터
        List<ShareDTO> chapterDTOList = shareService.getPersonalShareData(group_id,suggest_id, my_id);

        return CommonResponse.createResponse(200, "그룹 주제 조회 성공", chapterDTOList);
    }
}
