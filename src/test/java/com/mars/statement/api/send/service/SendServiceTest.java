package com.mars.statement.api.send.service;

import com.mars.statement.api.send.dto.SendMessageDTO;
import com.mars.statement.api.send.repository.SendRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendServiceTest {

    @Autowired
    private SendService sendService;

    @Test
    void saveSendMessage() {

        List<SendMessageDTO> messageDTOList = Arrays.asList(
                new SendMessageDTO(1L,"test"),
                new SendMessageDTO(2L,"test")
        );

//        int result = sendService.saveSendMessage(messageDTOList, 1L);

//        assertEquals(0);
    }
}