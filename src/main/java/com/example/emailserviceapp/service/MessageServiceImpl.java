package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Message;
import com.example.emailserviceapp.models.Type;
import com.example.emailserviceapp.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageRepository messageRepository;


    @Autowired
    MailboxesServiceImpl mailboxesService;

    @Override
    public void sendMessage(MessageRequest message, String senderEmail) {
        Message incomingMessage = new Message();
        incomingMessage.setMessageBody(message.getMessageBody());
        incomingMessage.setLocalDateTime(LocalDateTime.now());
        incomingMessage.setSender(senderEmail);

        incomingMessage.setReceiver(message.getRecipientEmailAddress());

        mailboxesService.addMessages(incomingMessage);
        messageRepository.save(incomingMessage);

    }

    @Override
    public void readMessage(MessageRequest message,String email) {
        Message incomingMessage = new Message();

        incomingMessage.setMessageBody(message.getMessageBody());
        incomingMessage.setLocalDateTime(LocalDateTime.now());
        incomingMessage.setSender(email);

        incomingMessage.setReceiver(message.getRecipientEmailAddress());
        incomingMessage.setRead(true);

        mailboxesService.addMessages(incomingMessage);
    }

    @Override
    public void deleteMessage() {

    }

    @Override
    public void sendBulkEmail(BulkMessageRequest message, String senderEmail) {
        Message incomingMessage = new Message();
        incomingMessage.setMessageBody(message.getMessageBody());
        incomingMessage.setLocalDateTime(LocalDateTime.now());
        incomingMessage.setSender(senderEmail);

        incomingMessage.setReceiver(message.getEmails().get(0));
    }


}
