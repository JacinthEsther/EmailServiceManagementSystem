package com.example.emailserviceapp.service;


import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Mailboxes;
import com.example.emailserviceapp.models.Message;
import com.example.emailserviceapp.models.Type;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailboxesServiceImpl implements MailboxesService {

    @Autowired
    private MailboxesRepository repository;


    @Override
    public String createMailboxes(String email) {
        Mailboxes mailboxes = new Mailboxes();

        Mailbox mailbox = new Mailbox();

        Message message = new Message();
        message.setSender("EmailServiceApp.com");
        message.setReceiver(email);
        message.setLocalDateTime(LocalDateTime.now());
        message.setMessageBody("Welcome "+ email);

        mailbox.setType(Type.INBOX);
        mailbox.setMessage(message);

        mailboxes.setEmail(email);
        mailboxes.getMailbox().add(mailbox);


        repository.save(mailboxes);
        return "successful";
    }

    @Override
    public void addMessages(Message message) {
        Mailboxes mailboxes1= repository.findById(message.getReceiver()).orElseThrow(()->
                new EmailException("email not found"));
        Mailboxes mailboxes2 = repository.findById(message.getSender()).
                orElseThrow(()-> new EmailException("sender email not found"));


        Mailbox mailbox= new Mailbox();

        mailbox.setMessage(message);
        mailbox.setEmail(message.getReceiver());
        mailbox.setType(Type.INBOX);



        Mailbox mailbox1 = new Mailbox();

        mailbox1.setMessage(message);
        mailbox1.setEmail(message.getSender());
        mailbox1.setType(Type.SENT);


        mailboxes1.getMailbox().add(mailbox);
        mailboxes2.getMailbox().add(mailbox1);


        repository.save(mailboxes1);
        repository.save(mailboxes2);

    }
}
