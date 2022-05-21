package com.example.emailserviceapp.service;


import com.example.emailserviceapp.models.Mailboxes;
import com.example.emailserviceapp.repositories.MailboxesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailboxesServiceImpl implements MailboxesService {

    @Autowired
    private MailboxesRepository repository;
    @Override
    public String createMailboxes(Mailboxes mailboxes) {

        repository.save(mailboxes);
        return "successful";
    }
}
