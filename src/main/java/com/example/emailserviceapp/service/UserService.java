package com.example.emailserviceapp.service;

import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.SignUpResponse;
import com.example.emailserviceapp.dtos.UpdateRequest;
import com.example.emailserviceapp.dtos.UpdateResponse;

public interface UserService {

    SignUpResponse signUp(SignUpRequest request);

    UpdateResponse update(UpdateRequest request);
}
