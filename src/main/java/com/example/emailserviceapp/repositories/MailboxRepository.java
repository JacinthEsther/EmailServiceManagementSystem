package com.example.emailserviceapp.repositories;

import com.example.emailserviceapp.models.Mailbox;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxRepository extends MongoRepository<Mailbox, String> {
}
