package com.example.emailserviceapp.models;

import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageTest {

    @Autowired
    MessageServiceImpl messageService;

//    @Test
//    void messageCanBeSentToAnotherUser(){
//        MessageRequest message = new MessageRequest();
//        message.setMessageBody("Hello how are you doing?");
//        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");
//
//        messageService.sendMessage(message, "jacinta@gmail.com");
//
//
//
//
//
//    }
}
