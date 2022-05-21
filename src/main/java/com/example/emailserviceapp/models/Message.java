package com.example.emailserviceapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    private String messageId;
    private String sender;
    private String receiver;
    private boolean isRead;
    private Type type;
    private LocalDateTime localDateTime;
}
