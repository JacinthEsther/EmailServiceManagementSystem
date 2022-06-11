package com.example.emailserviceapp.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
@EqualsAndHashCode
@ToString
public class Mailboxes {
    @Id
    private String email;
    private ArrayList<Mailbox> mailbox= new ArrayList<>();

}
