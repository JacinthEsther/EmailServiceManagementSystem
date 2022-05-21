package com.example.emailserviceapp.controllers;


import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.service.MessageServiceImpl;
import com.example.emailserviceapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("api/v1/emailService")
public class Controller {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){
        return new ResponseEntity<>(userService.signUp(request), HttpStatus.CREATED);

    }

    @PatchMapping("/{sendersEmail}")
    public ResponseEntity <?> sendMessage(@RequestBody MessageRequest message,@PathVariable
            String sendersEmail){
        messageService.sendMessage(message,sendersEmail);
        return new ResponseEntity<>("Success" ,HttpStatus.OK);

    }
 }
