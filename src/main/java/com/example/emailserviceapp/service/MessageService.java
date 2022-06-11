package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.models.Message;

public interface MessageService {

    Message sendMessage(MessageRequest message, String senderEmail);

    Message readMessage(String messageId, String email);


    void deleteMessageFromInbox(String id, String email);

    Message sendMessage(BulkMessageRequest message, String senderEmail);

    Message searchForMessage(String id);


}
