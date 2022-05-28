package com.example.emailserviceapp.dtos.messages;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class BulkMessageRequest {

    private List <String> emails;
    private String messageBody;
}
