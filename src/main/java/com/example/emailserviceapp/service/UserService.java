package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.*;
import com.example.emailserviceapp.models.User;

import java.util.List;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    UpdateResponse update(UpdateRequest request);

    LoginResponse login(LoginRequest loginRequest);

    void deleteAll();

    SignUpResponse findBy(String email);

    List<User> findAllUsers();


}
