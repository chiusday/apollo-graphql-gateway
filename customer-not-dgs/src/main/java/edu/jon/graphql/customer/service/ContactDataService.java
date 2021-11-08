package edu.jon.graphql.customer.service;

import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.ContactType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class ContactDataService {
    public List<Contact> contacts(UUID customerId) {
        log.info("Fetching contacts for Customer: {}", customerId.toString());
        return List.of(
                Contact.builder()
                        .id(UUID.randomUUID())
                        .customerId(customerId)
                        .contactType(ContactType.MOBILE)
                        .value(String.valueOf(new Random().nextInt(99999999)))
                        .build(),
                Contact.builder()
                        .id(UUID.randomUUID())
                        .customerId(customerId)
                        .contactType(ContactType.EMAIL)
                        .value(customerId.toString().substring(0, 8)+"@gmail.com")
                        .build()
        );
    }
}
