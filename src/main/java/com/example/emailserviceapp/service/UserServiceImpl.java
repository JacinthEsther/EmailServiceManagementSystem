package com.example.emailserviceapp.service;


import com.example.emailserviceapp.dtos.SignUpRequest;
import com.example.emailserviceapp.dtos.SignUpResponse;
import com.example.emailserviceapp.dtos.UpdateRequest;
import com.example.emailserviceapp.dtos.UpdateResponse;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.Mailbox;
import com.example.emailserviceapp.models.Mailboxes;
import com.example.emailserviceapp.models.User;
import com.example.emailserviceapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailboxesServiceImpl service;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        String email = email(request.getEmail());

        String password = password(request.getPassword());

        User user =User.builder()
                .password(password)
                .email(email)
                .fullName(request.getFirstName()+ " "+ request.getLastName())
                .build();

        Mailbox mailbox = new Mailbox();
        mailbox.setEmail(user.getEmail());


        Mailboxes mailboxes = new Mailboxes();
        mailboxes.setEmail(mailbox.getEmail());

        mailboxes.setMailbox(mailbox);


        service.createMailboxes(mailboxes);

     User registeredUser=   userRepository.save(user);
     SignUpResponse response = new SignUpResponse();

     response.setEmail(registeredUser.getEmail());
     response.setMessage("welcome "+ registeredUser.getEmail());
     return response;
    }

    @Override
    public UpdateResponse update(UpdateRequest request) {
      boolean isUpdated = false;
      User user=  userRepository.findById(request.getEmail()).orElseThrow(
              ()->new EmailException("user not found")
      );
        UpdateResponse response = new UpdateResponse();

            if (!(request.getPassword() == null || request.getPassword().trim().equals(""))) {
                String password = password(request.getPassword());
                user.setPassword(password);
                isUpdated = true;
            }
            if (!(request.getFirstName() == null || request.getFirstName().trim().equals(""))
                    || (!(request.getLastName() == null || request.getLastName().trim().equals("")))) {
                user.setFullName(request.getFirstName() + " " + request.getLastName());
                isUpdated = true;
            }
            if (isUpdated) {
              User updatedUser=  userRepository.save(user);
              response.setEmail(updatedUser.getEmail());
              response.setPassword(updatedUser.getPassword());
              response.setFullName(updatedUser.getFullName());
              response.setMessage("updated successfully");
//                return response;



        return response;
            }
            throw new EmailException("not updated");

    }

    private String password(String request) {
        String password="";
        String regex1 = "^(?=[^A-Z\\s]*[A-Z])(?=[^a-z\\s]*[a-z])(?=[^\\d\\s]*\\d)(?=\\w*[\\W_])\\S{8,}$";

        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(request);
        if (matcher1.matches()) {
            password = request;
        }
        else throw new EmailException("password must contain at Least Eight Characters" +
                "with at least one Special Character, Capital and Small Letter ");
        return password;
    }

    private String email(String request) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        String email="";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(request);
        if (matcher.matches()) {
         email    = request;
        }
        else throw new EmailException("enter a valid email");
        return email;
    }

}
