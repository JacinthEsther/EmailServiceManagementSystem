package com.example.emailserviceapp.models;


import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mailboxes {
    @Id
    private String email;
    private Mailbox mailbox;

}
