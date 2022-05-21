package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Message;
import com.example.emailserviceapp.models.Type;
import com.example.emailserviceapp.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageRepository messageRepository;


    @Autowired
    MailboxesServiceImpl mailboxesService;

    @Override
    public void sendMessage(MessageRequest message, String sendersEmail) {
        Message incomingMessage = new Message();
        incomingMessage.setMessageBody(message.getMessageBody());
        incomingMessage.setLocalDateTime(LocalDateTime.now());
        incomingMessage.setSender(sendersEmail);

        incomingMessage.setReceiver(message.getRecipientEmailAddress());


        mailboxesService.addMessages(incomingMessage);

    }
}
