package com.example.emailserviceapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Notification {

    private String messageId;
    private String email;
    private String title;
    private String message;

}
