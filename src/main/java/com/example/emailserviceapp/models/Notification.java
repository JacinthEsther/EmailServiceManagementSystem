package com.example.emailserviceapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
    @Id
    private String id;
    private String messageId;
    private String title;
    private Message message;

}
