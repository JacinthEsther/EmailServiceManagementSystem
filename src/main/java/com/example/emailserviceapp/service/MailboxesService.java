package com.example.emailserviceapp.service;

import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Mailboxes;
import com.example.emailserviceapp.models.Message;
import com.example.emailserviceapp.models.Notification;

import java.util.List;

public interface MailboxesService {
    Notification createMailboxes(String email);

    void addMessages(Message message);

    List<Message> viewAllInboxes(String email);

    List<Message> viewAllOutboxes(String email);


    void checkReadMessage(Message incomingMessage, String email);
}
