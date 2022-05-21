package com.example.emailserviceapp.models;

import com.example.emailserviceapp.dtos.*;
import com.example.emailserviceapp.exceptions.EmailException;
import com.example.emailserviceapp.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserTest {

    @Autowired
 private UserServiceImpl userService;

    private  SignUpRequest user;
    @BeforeEach
    void setUp() {
         user = SignUpRequest.builder()
                .email("agbonirojacinta@gmail.com")
                .password("Jacinta@5")
                .firstName("Esther")
                .lastName("Jacinta")
                .build();
    }

@Test
void testForUserCanSignUp(){

         SignUpResponse response= userService.signUp(user);
         assertThat(response.getEmail()).isEqualTo("agbonirojacinta@gmail.com");
         assertThat(response.getMessage()).isEqualTo("welcome "+ response.getEmail() );
}

@Test
void testForOnlyValidEmailUserCanBeSaved(){
    SignUpRequest user = SignUpRequest.builder()
            .email("agbonirojacinta.com")
            .password("Jacinta@5")
            .firstName("Esther")
            .lastName("Jacinta")
            .build();

    assertThrows(EmailException.class,()->userService.signUp(user));
}

@Test
void testForOnlyValidPasswordCanBeSaved(){
    SignUpRequest user = SignUpRequest.builder()
            .email("agbonirojacinta@gmail.com")
            .password("Jacinta")
            .firstName("Esther")
            .lastName("Jacinta")
            .build();
    assertThrows(EmailException.class,()->userService.signUp(user));
}

@Test
void testToUpdateUserCredentials(){
     userService.signUp(user);
    UpdateRequest request= new UpdateRequest();
    request.setEmail("agbonirojacinta@gmail.com");
    request.setPassword("Esther12_&");
    request.setFirstName("Jacinta");
    request.setLastName("Esther");


    UpdateResponse updatedResponse = userService.update(request);
    assertThat(updatedResponse.getMessage()).isEqualTo("updated successfully");
    assertThat(updatedResponse.getFullName()).isEqualTo("Jacinta Esther");
    assertThat(updatedResponse.getPassword()).isEqualTo("Esther12_&");

}

@Test
void userCanLogin(){
        userService.signUp(user);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("Jacinta@5");
        loginRequest.setEmail("agbonirojacinta@gmail.com");

        LoginResponse response=userService.login(loginRequest);
        assertThat(response.getMessage()).isEqualTo("Welcome agbonirojacinta@gmail.com");
}


    @AfterEach
    void tearDown() {
    }
}