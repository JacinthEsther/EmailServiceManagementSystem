package com.example.emailserviceapp.service;

import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Mailboxes;
import com.example.emailserviceapp.models.Message;

public interface MailboxesService {
    String createMailboxes(String email);

    void addMessages(Message message);
}
