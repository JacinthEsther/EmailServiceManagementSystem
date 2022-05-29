package com.example.emailserviceapp.controllers;


import com.example.emailserviceapp.dtos.LoginRequest;
import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.service.MailboxesServiceImpl;
import com.example.emailserviceapp.service.MessageServiceImpl;
import com.example.emailserviceapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/emailService")
public class Controller {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private MailboxesServiceImpl mailboxesService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){
        return new ResponseEntity<>(userService.signUp(request), HttpStatus.CREATED);

    }

    @PostMapping("/sendMessage/{sendersEmail}")
    public ResponseEntity <?> sendMessage(@RequestBody MessageRequest message,@PathVariable
            String sendersEmail){
        messageService.sendMessage(message,sendersEmail);
        return new ResponseEntity<>("Success" ,HttpStatus.OK);

    }

     @PostMapping("/login/email")
        public ResponseEntity <?> login(@RequestBody LoginRequest request){
            userService.login(request);
            return new ResponseEntity<>("Success" ,HttpStatus.OK);

        }

    @PostMapping("/send/messages/{sendersEmail}")
    public ResponseEntity <?> sendMessages(@RequestBody BulkMessageRequest message, @PathVariable
            String sendersEmail){
        messageService.sendMessage(message,sendersEmail);
        return new ResponseEntity<>("Success" ,HttpStatus.OK);

    }

    @GetMapping("/{messageId}/{email}")
    public ResponseEntity <?> readMessage( @PathVariable String messageId,
                                           @PathVariable String email){
        messageService.readMessage(messageId, email);
        return new ResponseEntity<>("Success" ,HttpStatus.OK);

    }
    @GetMapping("/{messageId}")
    public ResponseEntity <?> searchMessage( @PathVariable String messageId){

        return new ResponseEntity<>(messageService.searchForMessage(messageId) ,HttpStatus.OK);

    }

    @GetMapping("/inbox/{email}")
    public ResponseEntity <?> getAllInboxes(@PathVariable String email){

        return new ResponseEntity<>(mailboxesService.viewAllInboxes(email) ,HttpStatus.OK);

    }

    @GetMapping("/all/outbox/{email}")
    public ResponseEntity <?> getAllOutboxes(@PathVariable String email){

        return new ResponseEntity<>(mailboxesService.viewAllOutboxes(email) ,HttpStatus.OK);

    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap();
        exception.getBindingResult().getAllErrors().forEach(
                (error ->{
                    String fieldName= ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                })
        );
        return errors;
    }
 }
