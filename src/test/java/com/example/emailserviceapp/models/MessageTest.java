package com.example.emailserviceapp.models;

import com.example.emailserviceapp.dtos.LoginRequest;
import com.example.emailserviceapp.dtos.LoginResponse;
import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.SignUpResponse;
import com.example.emailserviceapp.dtos.messages.BulkMessageRequest;
import com.example.emailserviceapp.dtos.messages.MessageRequest;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import com.example.emailserviceapp.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MessageTest {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    MailboxesService mailboxesService;

    @Autowired
    MailboxesRepository mailboxes;

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

       String response=messageService.sendMessage(message, "jacinta@gmail.com");

     assertThat(response).isEqualTo("message sent successfully");

     Optional<Mailboxes> receiver=mailboxes.findById(signupUser.getEmail());
     Optional<Mailboxes> sender=mailboxes.findById(signupUser1.getEmail());


        assertThat(receiver.get().getMailbox().get(1).getMessage().size()
        ).isEqualTo(0);
        assertThat(receiver.get().getMailbox().get(0).getMessage().size())
                .isEqualTo(2);


          assertThat(sender.get().getMailbox().get(1).getMessage().size()
        ).isEqualTo(1);
        assertThat(sender.get().getMailbox().get(0).getMessage().size())
                .isEqualTo(1);


    }


    @Test
    void messageBodyCannotBeEmptyTest(){

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
        message.setMessageBody(null);
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");

        assertThrows(EmailException.class,()->
                messageService.sendMessage(message, "jacinta@gmail.com"));
    }

    @Test
    void userCanSendBulkEmailTest(){
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
    void messageCanBeReadTest(){
        userService.signUp(user);
        userService.signUp(user1);
        MessageRequest message = new MessageRequest();
        message.setMessageBody("Hello how are you doing?");
        message.setRecipientEmailAddress("agbonirojacinta@gmail.com");


//        messageService.readMessage(message,"jacinta@gmail.com");

    }

    @Test
    void userCanViewAllInboxTest(){
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
       List<Message> inbox= mailboxesService.viewAllInboxes("agbonirojacinta@gmail.com");
       assertThat(inbox.size()).isEqualTo(2);

    }


    @Test
    void userCanViewAllOutboxTest(){
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


        List<Message> outbox= mailboxesService.viewAllOutboxes("jacinta@gmail.com");
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
