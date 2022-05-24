package com.example.emailserviceapp.service;

import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Message;

import java.util.List;

public interface MailboxesService {
    String createMailboxes(String email);

    void addMessages(Message message);

    List<Mailbox> viewAllInboxes(String email);

    List<Mailbox> viewAllOutboxes(String email);
}
