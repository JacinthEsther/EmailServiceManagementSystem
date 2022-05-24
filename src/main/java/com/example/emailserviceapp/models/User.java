package com.example.emailserviceapp.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {
    @Id
    @Email

    private String email;
    @Min(5)
    @NotEmpty
    private String password;
    @NotEmpty
    private String fullName;
    private boolean isLoggedIn;
    private List<Notification> newNotifications= new ArrayList<>();
}
