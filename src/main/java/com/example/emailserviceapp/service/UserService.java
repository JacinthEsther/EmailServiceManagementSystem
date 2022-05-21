package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.*;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    UpdateResponse update(UpdateRequest request);

    LoginResponse login(LoginRequest loginRequest);

}
