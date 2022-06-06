package com.example.emailserviceapp.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {

    private String email;
    private String password;
}
