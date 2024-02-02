package com.mars.statement.api.send.service;

import com.mars.statement.api.send.dto.SendMessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SendServiceTest {

    @Autowired
    private SendService sendService;

    @Test
    void saveSendMessage() {

        List<SendMessageDto> messageDtoList = Arrays.asList(
                new SendMessageDto(1L,"test"),
                new SendMessageDto(2L,"test")
        );

//        int result = sendService.saveSendMessage(messageDtoList, 1L);

//        assertEquals(0);
    }
}