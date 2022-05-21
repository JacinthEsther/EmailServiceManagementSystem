package com.example.emailserviceapp.repositories;

import com.example.emailserviceapp.models.Mailboxes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxesRepository extends MongoRepository<Mailboxes, String> {
}
