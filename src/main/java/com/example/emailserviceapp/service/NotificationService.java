package com.example.emailserviceapp.service;

import com.example.emailserviceapp.models.Notification;

public interface NotificationService {
    Notification getNewNotifications(Notification notification);
}
