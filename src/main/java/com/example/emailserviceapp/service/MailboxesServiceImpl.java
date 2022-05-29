package com.example.emailserviceapp.service;


import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.*;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import com.example.emailserviceapp.repositories.MessageRepository;
import com.example.emailserviceapp.repositories.NotificationRepository;
import com.example.emailserviceapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MailboxesServiceImpl implements MailboxesService {

    @Autowired
    private MailboxesRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Notification createMailboxes(String email) {
        Mailboxes mailboxes = new Mailboxes();

        Notification notification = new Notification();

        Mailbox mailbox = new Mailbox();

        Message message = new Message();
        message.setSender("EmailServiceApp.com");
        message.getReceivers().add(email);
        message.setLocalDateTime(LocalDateTime.now());
        message.setMessageBody("Welcome "+ email);

      Message savedMessage=  messageRepository.save(message);
        mailbox.setType(Type.INBOX);
        mailbox.getMessage().add(savedMessage);

        mailboxes.setEmail(email);
        mailboxes.getMailbox().add(mailbox);

      repository.save(mailboxes);
        notification.setMessageId(savedMessage.getMessageId());
        notification.setMessage(savedMessage);
        notification.setTitle("New Incoming Message from "+notification.getMessage().getMessageBody());

        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public void addMessages(Message message) {
        Mailboxes mailboxesReceivers;

//
//       message.getReceivers().forEach(receiver -> repository.findById(receiver).orElseThrow(
//               () ->
//                       new EmailException("email not found")
//       ));
        for (int i = 0; i < message.getReceivers().size(); i++) {
            mailboxesReceivers = repository.findById(message.getReceivers().get(i)).orElseThrow(
                    () ->
                            new EmailException("email not found")
            );


            Mailboxes mailboxesSender = repository.findById(message.getSender()).
                    orElseThrow(() -> new EmailException("sender email not found"));


            User user = userRepository.findById(message.getReceivers().get(i)).orElseThrow();

            if(user.isLoggedIn()) {
                Mailbox receivers = new Mailbox();

                receivers.getMessage().add(message);

                receivers.setEmail(message.getReceivers().get(i));
                receivers.setType(Type.INBOX);


                Notification notification = new Notification();

                Mailbox sender = new Mailbox();

                sender.getMessage().add(message);

                sender.setEmail(message.getSender());
                sender.setType(Type.SENT);


                mailboxesReceivers.getMailbox().add(receivers);
                mailboxesSender.getMailbox().add(sender);

                notification.setMessageId(message.getMessageId());


                notification.setMessage(message);
                notification.setTitle("New Incoming Message from " + notification.getMessage());
                Notification savedNotification = notificationRepository.save(notification);
                user.getNewNotifications().add(savedNotification);
                userRepository.save(user);


                repository.save(mailboxesReceivers);
                repository.save(mailboxesSender);
            }
        else throw new EmailException("user is not logged in");


    }
    }

    @Override
    public List<Mailbox> viewAllInboxes(String email) {
      Mailboxes mailboxes=  repository.findById(email).orElseThrow(
              ()-> new EmailException("email does not exist")
      );
        User user = userRepository.findById(email).orElseThrow(
                ()-> new EmailException("user not found")
        );
        if(user.isLoggedIn()) {

        return mailboxes.getMailbox().stream()
                .parallel()
                .filter(mailbox-> mailbox.getType()==Type.INBOX)
                .collect(Collectors.toList());
        }
        throw new EmailException("user is not logged in");

    }

    @Override
    public List<Mailbox> viewAllOutboxes(String email) {
        Mailboxes mailboxes=  repository.findById(email).orElseThrow(
                ()-> new EmailException("email does not exist")
        );
        User user = userRepository.findById(email).orElseThrow(
                ()-> new EmailException("user not found")
        );
        if(user.isLoggedIn()) {
            return mailboxes.getMailbox().stream()
                    .parallel()
                    .filter(mailbox -> mailbox.getType() == Type.SENT)
                    .collect(Collectors.toList());
        }
        throw new EmailException("user is not logged in");
    }



    @Override
    public void checkReadMessage(Message incomingMessage, String email) {

       Optional<Notification> notification= notificationRepository
               .findById(incomingMessage.getMessageId());

       if(notification.isPresent()) {
           notification.get().getMessage().setRead(true);
        Notification savedNotification= notificationRepository.save(notification.get());

           User user = userRepository.findById(email).orElseThrow();
        if(user.isLoggedIn()) {
            for (int i = 0; i < user.getNewNotifications().size(); i++) {
                if (Objects.equals(user.getNewNotifications().get(i).getMessageId(),
                        savedNotification.getMessageId())) {
                    user.getNewNotifications().remove(user.getNewNotifications().get(i));
                    userRepository.save(user);
                }
            }


            Mailboxes mailboxes = repository.findById(email).orElseThrow();

            ArrayList<Mailbox> mailboxes2 = mailboxes.getMailbox();
            for (Mailbox mailbox : mailboxes2) {
                for (int j = 0; j < mailbox.getMessage().size(); j++) {
                    if (Objects.equals(mailbox.getMessage()
                            .get(j).getMessageId(), incomingMessage.getMessageId())) {
                        mailbox.getMessage().get(j).setRead(true);
                    }
                    repository.save(mailboxes);
                }
            }
        }

   }



    }
}
