package com.example.emailserviceapp.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Message {
    @Id
    private String messageId;
    private String sender;
    private String receiver;
    private boolean isRead;
    private LocalDateTime localDateTime;
    private String messageBody;
}
