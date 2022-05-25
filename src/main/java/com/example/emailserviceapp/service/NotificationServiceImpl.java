package com.example.emailserviceapp.service;

import com.example.emailserviceapp.models.Notification;
import com.example.emailserviceapp.models.User;
import com.example.emailserviceapp.repositories.NotificationRepository;
import com.example.emailserviceapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public Notification getNewNotifications(Notification notification) {

return null;

        }


//        if(Objects.equals(notification.getMessage().getSender(), "EmailServiceApp.com")){
//            if(userRepository.findById(notification.getMessage().getReceivers().get(0)).isPresent())
//            user.getNewNotifications().add(notification);
//            userRepository.save(user);
//
//        }
//       else {
//            for (int i = 0; i < notification.getMessage().getReceivers().size(); i++) {
//                user = userRepository.findById(notification.getMessage().getSender()).orElseThrow();
//
//                user.getNewNotifications().add(notification);
//                userRepository.save(user);
//            }
//        }
//            notificationRepository.save(notification);
//            return notification;

}
