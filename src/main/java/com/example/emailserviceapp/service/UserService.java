package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.*;
import com.example.emailserviceapp.models.Notification;
import com.example.emailserviceapp.models.User;

import java.util.List;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    UpdateResponse update(UpdateRequest request);

    LoginResponse login(LoginRequest loginRequest);

    void deleteAll();

    User findUserBy(String email);

    List<User> findAllUsers();




//    void getNewNotifications(Notification notification);
}
