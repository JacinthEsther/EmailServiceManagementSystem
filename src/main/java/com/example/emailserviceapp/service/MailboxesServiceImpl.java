package com.example.emailserviceapp.service;


import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.*;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import com.example.emailserviceapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailboxesServiceImpl implements MailboxesService {

    @Autowired
    private MailboxesRepository repository;

    @Autowired
    private UserService userService;


    @Override
    public String createMailboxes(String email) {
        Mailboxes mailboxes = new Mailboxes();

        Notification notification = new Notification();

        Mailbox mailbox = new Mailbox();

        Message message = new Message();
        message.setSender("EmailServiceApp.com");
        message.getReceivers().add(email);
        message.setLocalDateTime(LocalDateTime.now());
        message.setMessageBody("Welcome "+ email);

        mailbox.setType(Type.INBOX);
        mailbox.getMessage().add(message);

        mailboxes.setEmail(email);
        mailboxes.getMailbox().add(mailbox);
//
        notification.setMessageId(message.getMessageId());
        notification.setMessage(message.getMessageBody());
        notification.setEmail("EmailServiceApp.com");
        notification.setTitle("New Incoming Message from "+notification.getEmail());
//        userService.getNewNotifications(notification);

        repository.save(mailboxes);
        return "successful";
    }

    @Override
    public void addMessages(Message message) {
      Mailboxes mailboxes1 = new Mailboxes();

//      mailboxes1.getMailbox().stream()
//              .forEach(m -> repository.findById(m.setEmail(message.getReceivers().get(i))));
        for (int i = 0; i < message.getReceivers().size(); i++) {
             mailboxes1= repository.findById(message.getReceivers().get(i)).orElseThrow(
                    ()->
                            new EmailException("email not found")
            );
        }

        Mailboxes mailboxes2 = repository.findById(message.getSender()).
                orElseThrow(()-> new EmailException("sender email not found"));


        Mailbox mailbox= new Mailbox();

        mailbox.getMessage().add(message);

        mailbox.setEmail(message.getReceivers().get(0));
        mailbox.setType(Type.INBOX);


        Notification notification= new Notification();

        Mailbox mailbox1 = new Mailbox();

        mailbox1.getMessage().add(message);

        mailbox1.setEmail(message.getSender());
        mailbox1.setType(Type.SENT);


        mailboxes1.getMailbox().add(mailbox);
        mailboxes2.getMailbox().add(mailbox1);

        notification.setMessageId(message.getMessageId());
        notification.setMessage(message.getMessageBody());
        notification.setEmail(message.getSender());
        notification.setTitle("New Incoming Message from "+notification.getEmail());
//        userService.getNewNotifications(notification);

        repository.save(mailboxes1);
        repository.save(mailboxes2);

    }

    @Override
    public List<Mailbox> viewAllInboxes(String email) {
      Mailboxes mailboxes=  repository.findById(email).orElseThrow(
              ()-> new EmailException("email does not exist")
      );

        return mailboxes.getMailbox().stream()
                .parallel()
                .filter(mailbox-> mailbox.getType()==Type.INBOX)
                .collect(Collectors.toList());


    }

    @Override
    public List<Mailbox> viewAllOutboxes(String email) {
        Mailboxes mailboxes=  repository.findById(email).orElseThrow(
                ()-> new EmailException("email does not exist")
        );

        return mailboxes.getMailbox().stream()
                .parallel()
                .filter(mailbox-> mailbox.getType()==Type.SENT)
                .collect(Collectors.toList());
    }
}
