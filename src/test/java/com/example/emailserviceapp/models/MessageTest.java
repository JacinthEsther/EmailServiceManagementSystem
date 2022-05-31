package com.example.emailserviceapp.models;

import com.example.emailserviceapp.dtos.LoginRequest;
import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.SignUpResponse;
import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
//import com.example.emailserviceapp.dtos.messages.MessageResponse;
import com.example.emailserviceapp.service.MailboxesServiceImpl;
import com.example.emailserviceapp.service.MessageServiceImpl;
import com.example.emailserviceapp.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessageTest {

    @Autowired
    MessageServiceImpl messageService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    MailboxesServiceImpl mailboxesService;

    private SignUpRequest user;
    private SignUpRequest user1;
    private SignUpRequest user2;
    @BeforeEach
    void setUp() {
        user = SignUpRequest.builder()
                .email("agbonirojacinta@gmail.com")
                .password("Jacinta@5")
                .firstName("Esther")
                .lastName("Jacinta")
                .build();
         user1 = SignUpRequest.builder()
                .email("jacinta@gmail.com")
                .password("Jacinta@5")
                .firstName("Esther")
                .lastName("Jacinta")
                .build();
    }

    @Test
    void messageCanBeSentToAnotherUser(){
      SignUpResponse signupUser=  userService.signUp(user);
        SignUpResponse signupUser1= userService.signUp(user1);

        LoginRequest requestUser = new LoginRequest();
        requestUser.setEmail(signupUser.getEmail());
        requestUser.setPassword("Jacinta@5");

        userService.login(requestUser);

        LoginRequest requestUser1 = new LoginRequest();
        requestUser1.setEmail(signupUser1.getEmail());
        requestUser1.setPassword("Jacinta@5");

        userService.login(requestUser1);

        MessageRequest message = new MessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");

        messageService.sendMessage(message, "jacinta@gmail.com");
//        MessageResponse response= new MessageResponse();

//        Message newMessage = new Message();
//
//        assertThat(newMessage.getReceiver()).isEqualTo("agbonirojacinta@gmail.com");
//        assertThat(newMessage.getSender()).isEqualTo("jacinta@gmail.com");
//        assertThat(newMessage.getMessageBody()).isEqualTo("Hello how are you doing?");

    }

    @Test
    void userCanSendBulkEmail(){
        userService.signUp(user);
        userService.signUp(user1);
        user2 = SignUpRequest.builder()
                .email("jacintaEsther@gmail.com")
                .password("Jacinta@5")
                .firstName("Esther")
                .lastName("Jacinta")
                .build();


        BulkMessageRequest message = new BulkMessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.getEmails().add("agbonirojacinta@gmail.com");
        message.getEmails().add("jacintaEsther@gmail.com");

        messageService.sendMessage(message,"jacinta@gmail.com");
        userService.findUserBy("jacintaEsther@gmail.com");
    }

    @Test
    void messageCanBeRead(){
        userService.signUp(user);
        userService.signUp(user1);
        MessageRequest message = new MessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");


//        messageService.readMessage(message,"jacinta@gmail.com");

    }

    @Test
    void userCanViewAllInbox(){
        userService.signUp(user);
        userService.signUp(user1);
        MessageRequest message = new MessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");


//        messageService.readMessage(message,"jacinta@gmail.com");
       List<Mailbox> inbox= mailboxesService.viewAllInboxes("agbonirojacinta@gmail.com");
       assertThat(inbox.size()).isEqualTo(2);

    }


    @Test
    void userCanViewAllOutbox(){
        userService.signUp(user);
        userService.signUp(user1);
        MessageRequest message = new MessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");


//        messageService.readMessage(message,"jacinta@gmail.com");
        List<Mailbox> outbox= mailboxesService.viewAllOutboxes("jacinta@gmail.com");
        assertThat(outbox.size()).isEqualTo(1);
    }

        @Test
        void testThatMessageCanBeDeleted(){
            userService.signUp(user);
            userService.signUp(user1);
            MessageRequest message = new MessageRequest();
            message.setMessageBody("Hello how are you doing?");
            message.setRecipientEmailAddress("agbonirojacinta@gmail.com");

//            messageService.readMessage(message,"jacinta@gmail.com");
            //todo: complete delete implementation
            messageService.deleteMessage("");
        }


        @Test
        void searchForMessageTest(){

        }


    @AfterEach
    void tearDown() {
        userService.deleteAll();
    }
}
