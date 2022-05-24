package com.example.emailserviceapp.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> receivers = new ArrayList<>();
    private boolean isRead;
    private LocalDateTime localDateTime;
    private String messageBody;
}
