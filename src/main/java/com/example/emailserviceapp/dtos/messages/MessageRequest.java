package com.example.emailserviceapp.dtos.messages;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    private String messageBody;
    private String recipientEmailAddress;
}
