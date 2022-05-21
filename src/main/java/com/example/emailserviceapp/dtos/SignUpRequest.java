package com.example.emailserviceapp.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

}
