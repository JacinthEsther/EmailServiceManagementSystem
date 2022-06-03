package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.models.Message;

public interface MessageService {

    String sendMessage(MessageRequest message, String senderEmail);

    void readMessage(String messageId, String email);


    void deleteMessage(String id);

    void sendMessage(BulkMessageRequest message, String senderEmail);

    Message searchForMessage(String id);


}
