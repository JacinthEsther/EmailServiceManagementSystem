package com.example.emailserviceapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mailbox {

    private String email;
    private List<Message> message = new ArrayList<>();
    private Type type;
}
