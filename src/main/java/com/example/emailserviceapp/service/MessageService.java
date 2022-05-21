package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.messages.MessageRequest;

public interface MessageService {
    void sendMessage(MessageRequest message, String sendersEmail);
}
