package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.Message;
import com.example.emailserviceapp.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageRepository messageRepository;


    @Autowired
    MailboxesService mailboxesService;



    @Override
    public Message sendMessage(MessageRequest message, String senderEmail) {
        if(Optional.ofNullable(message.getMessageBody()).isEmpty()) {

        throw new EmailException("Message body cannot be empty");
        }
            Message incomingMessage = new Message();

            incomingMessage.setMessageBody(message.getMessageBody());
            incomingMessage.setLocalDateTime(LocalDateTime.now());
            incomingMessage.setSender(senderEmail);


            incomingMessage.getReceivers().add(message.getRecipientEmailAddress());

            Message newMessage = messageRepository.save(incomingMessage);
            mailboxesService.addMessages(newMessage);

            return newMessage;

    }

    @Override
    public Message readMessage(String messageId, String email) {


        Message incomingMessage =  messageRepository.findById(messageId).orElseThrow(()-> {
            throw new EmailException("message not found");
        });
        incomingMessage.setRead(true);
      Message savedMessage=  messageRepository.save(incomingMessage);

        mailboxesService.checkReadMessage(savedMessage, email);
        return savedMessage;
    }

    @Override
    public void deleteMessageFromInbox(String id, String email) {
     Message message  = messageRepository.findById(id).orElseThrow(
                ()-> new EmailException("not found")
        );
//       messageRepository.delete(message);
       mailboxesService.deleteMessageFromInbox(message, email);
    }

    @Override
    public Message sendMessage(BulkMessageRequest messages, String senderEmail) {
        Message incomingMessage = new Message();
        incomingMessage.setMessageBody(messages.getMessageBody());
        incomingMessage.setLocalDateTime(LocalDateTime.now());
        incomingMessage.setSender(senderEmail);

        messages.getEmails().forEach(email -> incomingMessage.getReceivers().add(email));
//        for (int i = 0; i < messages.getEmails().size(); i++) {
//            incomingMessage.getReceivers().add(messages.getEmails().get(i));
//        }

      Message newMessage=  messageRepository.save(incomingMessage);
        mailboxesService.addMessages(newMessage);
        return newMessage;

    }

    @Override
    public Message searchForMessage(String id) {
        return messageRepository.findById(id).orElseThrow(
                ()-> new EmailException("not found")
        );
    }


}
