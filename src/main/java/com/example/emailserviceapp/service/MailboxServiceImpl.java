package com.example.emailserviceapp.service;


import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class MailboxServiceImpl implements MailboxService{
@Autowired
private NotificationServiceImpl notificationService;

    @Override
    public void add(Mailbox mailbox) {

        Notification notification = new Notification();
        notification.setMessageId(mailbox.getMessageId());
        notification.setMessage(mailbox.getMessage().get(0));
        notification.setTitle("New Incoming Message from "+ notification.getMessage().getSender());
        notificationService.getNewNotifications(notification);
    }
}
