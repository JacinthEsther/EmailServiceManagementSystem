package com.example.emailserviceapp.service;


import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.*;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import com.example.emailserviceapp.repositories.NotificationRepository;
import com.example.emailserviceapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        mailbox.setType(Type.INBOX);
        mailbox.getMessage().add(message);

        mailboxes.setEmail(email);
        mailboxes.getMailbox().add(mailbox);

      repository.save(mailboxes);
        notification.setMessageId(message.getMessageId());
        notification.setMessage(message);
        notification.setTitle("New Incoming Message from "+notification.getMessage().getMessageBody());

        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public void addMessages(Message message) {
        Mailboxes mailboxes1 = new Mailboxes();

//      mailboxes1.getMailbox().stream()
//              .forEach(m -> repository.findById(m.setEmail(message.getReceivers().get(i))));
        for (int i = 0; i < message.getReceivers().size(); i++) {
            mailboxes1 = repository.findById(message.getReceivers().get(i)).orElseThrow(
                    () ->
                            new EmailException("email not found")
            );


            Mailboxes mailboxes2 = repository.findById(message.getSender()).
                    orElseThrow(() -> new EmailException("sender email not found"));


            Mailbox mailbox = new Mailbox();

            mailbox.getMessage().add(message);

            mailbox.setEmail(message.getReceivers().get(i));
            mailbox.setType(Type.INBOX);


            Notification notification = new Notification();

            Mailbox mailbox1 = new Mailbox();

            mailbox1.getMessage().add(message);

            mailbox1.setEmail(message.getSender());
            mailbox1.setType(Type.SENT);


            mailboxes1.getMailbox().add(mailbox);
            mailboxes2.getMailbox().add(mailbox1);

            notification.setMessageId(message.getMessageId());


            User user = userRepository.findById(message.getReceivers().get(i)).orElseThrow();
            notification.setMessage(message);
            notification.setTitle("New Incoming Message from " + notification.getMessage());
            Notification savedNotification = notificationRepository.save(notification);
            user.getNewNotifications().add(savedNotification);
            userRepository.save(user);


//       if(message.isRead()){
//           if(notification.getMessageId().equals(message.getMessageId())){
//               user.getNewNotifications().remove(notification);
//               userRepository.save(user);
//           }
//       }
        repository.save(mailboxes1);
        repository.save(mailboxes2);
    }
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



    @Override
    public void checkReadMessage(Message incomingMessage) {

   Optional<Notification> notification=notificationRepository.findById(incomingMessage.getMessageId());

   if(notification.isPresent()) {
       notification.get().getMessage().setRead(true);
       notificationRepository.save(notification.get());
       for (int i = 0; i < incomingMessage.getReceivers().size(); i++) {

           User user = userRepository.findById(incomingMessage.getReceivers().get(i)).orElseThrow();
           if(user.isLoggedIn()) {
               for (int j = 0; j < user.getNewNotifications().size(); j++) {
                   if (Objects.equals(user.getNewNotifications().get(j)
                           .getMessageId(), notification.get().getMessageId())) {

                       user.getNewNotifications().get(j).getMessage().setRead(true);

                       user.getNewNotifications().remove(notification.get());
                       userRepository.save(user);

                   }
               }
           }


            Mailboxes mailboxes =  repository.findById(incomingMessage.
                    getReceivers().get(i)).orElseThrow();

            if(Objects.equals(incomingMessage.getMessageId(),
                    mailboxes.getMailbox().get(i).getMessageId())) {
                mailboxes.getMailbox().get(i).getMessage().get(i).setRead(true);
                repository.save(mailboxes);
            }
       }
   }



    }
}
