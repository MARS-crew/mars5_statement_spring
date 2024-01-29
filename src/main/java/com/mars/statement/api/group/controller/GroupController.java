package com.mars.statement.api.group.controller;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final SuggestService suggestService;

    public GroupController(SuggestService suggestService){
        this.suggestService = suggestService;
    }

    @Tag(name="그룹", description = "그룹 주제 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description="그룹 조회 성공 ",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Suggest.class)))})
    })
    @GetMapping("/{group_id}")
    public ResponseEntity<Object> getGroupSuggest(@PathVariable Long group_id) {
        List<Suggest> suggest = suggestService.getSuggestByGroupId(group_id);

        return CommonResponse.createResponse(200, "그룹 주제 조회 성공", suggest);
    }


}
