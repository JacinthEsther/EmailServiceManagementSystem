package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;

public interface MessageService {
    void sendMessage(MessageRequest message, String senderEmail);

    void readMessage(MessageRequest message,String email);


    void deleteMessage();

    void sendBulkEmail(BulkMessageRequest message, String senderEmail);
}
