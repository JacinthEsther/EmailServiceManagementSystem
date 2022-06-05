package com.example.emailserviceapp.controllers;


import com.example.emailserviceapp.dtos.*;
import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
//import com.example.emailserviceapp.security.jwt.TokenProvider;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.User;
import com.example.emailserviceapp.security.jwt.TokenProvider;
import com.example.emailserviceapp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/emailService")
@CrossOrigin(origins = "http://localhost:3000/")
public class Controller {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MailboxesService mailboxesService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        SignUpResponse userDto = userService.signUp(request);

        ApiResponse response = ApiResponse.builder()
                .payLoad(userDto)
                .isSuccessful(true)
                .statusCode(201)
                .message("user created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/sendMessage/{sendersEmail}")
    public ResponseEntity <?> sendMessage(@RequestBody MessageRequest message,@PathVariable
            String sendersEmail){
        messageService.sendMessage(message,sendersEmail);
        return new ResponseEntity<>("Success" ,HttpStatus.OK);

    }

     @PostMapping("/login")
        public ResponseEntity <?> login(@RequestBody LoginRequest request){
        User user= userService.findUserBy(request.getEmail());
        if (bCryptPasswordEncoder.matches(user.getPassword(),request.getPassword())) {
            LoginResponse userDto = userService.login(request);
            ApiResponse response = ApiResponse.builder()
                    .payLoad(userDto)
                    .isSuccessful(true)
                    .statusCode(200)
                    .message("welcome " + request.getEmail())
                    .build();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword())

            );


            SecurityContextHolder.getContext().setAuthentication(authentication);

            final String token = tokenProvider.generateJWTToken(authentication);
//         User user= userService.findUserBy(request.getEmail());
            return new ResponseEntity<>(new AuthToken(response.getMessage()), HttpStatus.OK);
        }

       else throw new EmailException("user not found");


//            return new ResponseEntity<>(response ,HttpStatus.ACCEPTED);

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

//    @DeleteMapping
//    public ResponseEntity<?> delete( )


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
