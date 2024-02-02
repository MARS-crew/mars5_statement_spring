package com.mars.statement.api.send.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendDto {
    private Long chapter_id;

    private List<SendMessageDto> messageDtoList;



}
