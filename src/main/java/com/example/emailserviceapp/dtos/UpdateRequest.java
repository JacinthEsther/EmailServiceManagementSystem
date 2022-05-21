package com.example.emailserviceapp.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
