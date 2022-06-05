package com.example.emailserviceapp.service;


import com.example.emailserviceapp.dtos.*;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.models.*;
import com.example.emailserviceapp.repositories.NotificationRepository;
import com.example.emailserviceapp.repositories.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService , UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailboxesService service;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        String email = email(request.getEmail());

        String password = password(request.getPassword());

        User user =User.builder()
                .password(password)
                .email(email)
                .fullName(request.getFirstName()+ " "+ request.getLastName())
                .newNotifications(new ArrayList<>())
                .build();


        Notification  notification=  service.createMailboxes(user.getEmail());

//      notification.setMessage(mailboxes.getMailbox().get(0).getMessage().get(0));
////      Notification returnedNotification=  notification.getNewNotifications(user.getEmail());
      user.getNewNotifications().add(notification);


     User registeredUser =   userRepository.save(user);
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

        if(user.isLoggedIn()) {
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
                User updatedUser = userRepository.save(user);
                response.setEmail(updatedUser.getEmail());
                response.setPassword(updatedUser.getPassword());
                response.setFullName(updatedUser.getFullName());
                response.setMessage("updated successfully");
                return response;
            }
            throw new EmailException("not updated");
        }
        throw new EmailException("user must be logged in");

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
      User user=  userRepository.findById(loginRequest.getEmail())
              .orElseThrow(()->new EmailException("user not found"));
        if(!user.isLoggedIn()) {
//            if (user.getPassword().equals(loginRequest.getPassword())) {
                user.setLoggedIn(true);
                User savedUser = userRepository.save(user);

                LoginResponse response = new LoginResponse();
                response.setMessage("Welcome " + savedUser.getEmail());
                return response;
//            } else throw new EmailException("Invalid Details");
        }
        return new LoginResponse("user already logged in");
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User findUserBy(String email) {
        User user = userRepository.findById(email).orElseThrow(()->
                new EmailException("user does not exist"));

//        user.setLoggedIn(true);

        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        User user = new User();
        if(user.isLoggedIn()) {
            return userRepository.findAll();
        }
        throw new EmailException("user is not logged in");
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

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user =userRepository.findById(email).orElseThrow(()-> new EmailException("user not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(),

                getAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(
                role-> new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toSet());
    }
}
