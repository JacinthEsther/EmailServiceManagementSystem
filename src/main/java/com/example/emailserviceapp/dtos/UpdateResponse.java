package com.example.emailserviceapp.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private String email;
    private String password;
    private String fullName;
    private String message;
}
