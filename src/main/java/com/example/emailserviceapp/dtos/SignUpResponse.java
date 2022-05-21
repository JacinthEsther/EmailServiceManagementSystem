package com.example.emailserviceapp.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private String email;
    private String message;
}
